package com.salon.www.salonapi.security.service;

import com.salon.www.salonapi.dao.itf.UserDAO;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.security.JwtUserFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private UserDAO userDao;
    private JwtUserFactory jwtUserFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user =  userDao.findByUsername(username);

        if(user.isPresent()) {
            User foundUser = user.get();
            foundUser.setAuthorities(userDao.getCapabilitiesForUser(foundUser));
            return jwtUserFactory.create(foundUser);
        } else {
            log.error("No user found with username '{}'", username);
            throw new UsernameNotFoundException(String.format("No user found with username '%s'", username));
        }

    }
}
