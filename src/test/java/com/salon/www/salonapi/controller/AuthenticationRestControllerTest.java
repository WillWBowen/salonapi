package com.salon.www.salonapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salon.www.salonapi.model.security.Authority;
import com.salon.www.salonapi.model.security.AuthorityName;
import com.salon.www.salonapi.model.security.JwtAuthenticationRequest;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.security.JwtUser;
import com.salon.www.salonapi.security.JwtUserFactory;
import com.salon.www.salonapi.security.service.JwtUserDetailsService;
import org.h2.server.web.WebApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationRestControllerTest {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    public void successfulAuthenticationWthAnonymousUser() throws Exception {

        JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest("username", "password");

        mvc.perform(post("/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void successfulRefreshTokenWithUserRole() throws Exception {
        Authority authority = new Authority();
        authority.setId(0L);
        authority.setName(AuthorityName.ROLE_USER);
        List<Authority> authorities = Arrays.asList(authority);

        User user = new User();
        user.setUsername("username");
        user.setAuthorities(authorities);
        user.setEnabled(Boolean.TRUE);
        user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

        JwtUser jwtUser = JwtUserFactory.create(user);

        when(jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

        when(jwtUserDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

        when(jwtTokenUtil.canTokenBeRefreshed(any(), any())).thenReturn(true);

        mvc.perform(get("/refresh")
        .header("Authorization", "Bearer ThisIsARandomToken"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void successfulRefreshTokenWithAdminRole() throws Exception {
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName(AuthorityName.ROLE_ADMIN);
        List<Authority> authorities = Arrays.asList(authority);

        User user = new User();
        user.setUsername("admin");
        user.setAuthorities(authorities);
        user.setEnabled(Boolean.TRUE);
        user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

        JwtUser jwtUser = JwtUserFactory.create(user);

        when(jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

        when(jwtUserDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

        when(jwtTokenUtil.canTokenBeRefreshed(any(), any())).thenReturn(true);

        mvc.perform(get("/refresh")
                .header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {

        mvc.perform(get("/refresh"))
                .andExpect(status().isUnauthorized());
    }
}
