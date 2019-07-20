package com.salon.www.salonapi.security.service;

import com.salon.www.salonapi.dao.UserDAO;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.security.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    private UserDAO userDao;

    @Autowired
    public JwtUserDetailsService(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user =  userDao.findByUsername(username);

        if(user.isPresent()) {
            return JwtUserFactory.create(user.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'", username));
        }

    }
}
