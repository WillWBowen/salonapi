package com.salon.www.salonapi.security.controller;

import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRestController {

    private String tokenHeader;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    @Autowired
    public UserRestController(JwtTokenUtil jwtTokenUtil,
                              @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
                              @Value("${jwt.header}") String tokenHeader) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.tokenHeader = tokenHeader;
    }

    @RequestMapping(value="user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }
}
