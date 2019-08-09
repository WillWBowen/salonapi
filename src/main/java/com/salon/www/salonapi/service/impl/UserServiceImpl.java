package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.UserDAO;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.service.itf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private UserDAO userDao;

    @Autowired
    public UserServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }
    @Override
    public void createUser(User user) {
        userDao.save(user);
    }

    @Override
    public User getUser(String username) {
        return userDao.findByUsername(username).orElse(null);
    }
}
