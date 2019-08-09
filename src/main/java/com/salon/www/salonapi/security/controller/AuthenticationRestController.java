package com.salon.www.salonapi.security.controller;

import com.google.gson.Gson;
import com.salon.www.salonapi.model.security.JwtAuthenticationRequest;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.model.security.JwtAuthenticationResponse;
import com.salon.www.salonapi.security.JwtUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Log4j2
@RestController
public class AuthenticationRestController {

    private String tokenHeader;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    private static final Gson gson = new Gson();

    @Autowired
    public AuthenticationRestController(JwtTokenUtil jwtTokenUtil,
                              @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
                              @Value("${jwt.header}") String tokenHeader,
                              AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.tokenHeader = tokenHeader;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value="${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws JwtAuthenticationException {
        log.info("Request received, running createAuthenticationToken");
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(gson.toJson(new JwtAuthenticationResponse(token)));
    }

    @RequestMapping(value="${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if(jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(gson.toJson(new JwtAuthenticationResponse(refreshedToken)));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(JwtAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(e.getMessage()));
    }

    private void authenticate(String username, String password) throws JwtAuthenticationException {
        log.info("Authenticating username and password");
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new JwtAuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new JwtAuthenticationException("Bad credentials!", e);
        }
    }

}
