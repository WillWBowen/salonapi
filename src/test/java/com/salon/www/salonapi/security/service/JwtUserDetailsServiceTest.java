package com.salon.www.salonapi.security.service;

import com.salon.www.salonapi.dao.itf.UserDAO;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.security.JwtUser;
import com.salon.www.salonapi.security.JwtUserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailsServiceTest {

    @Mock
    private UserDAO userDao;

    @Mock
    private JwtUserFactory jwtUserFactory;

    private UserDetailsService userDetailsService;

    @Before()
    public void setUp() {
        this.userDetailsService = new JwtUserDetailsService(userDao, jwtUserFactory);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_shouldThrowUsernameNotFoundException() {
        given(userDao.findByUsername(anyString())).willReturn(Optional.empty());

        userDetailsService.loadUserByUsername("user");
    }

    @Test
    public void loadUserByUsername_() {
        User user = new User(1L, "username", "password", "test@email.com", true, null, null);
        JwtUser jwtUser = new JwtUser(1L, "username", "test@email.com", "password", null, true, null);
        given(userDao.findByUsername(anyString())).willReturn(Optional.of(user));
        given(jwtUserFactory.create(any())).willReturn(jwtUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("username");

        assertThat(userDetails.getUsername()).isEqualTo("username");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities()).isNull();

    }
}
