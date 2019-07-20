package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.model.Role;
import com.salon.www.salonapi.model.security.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends Dao<User> {
    Optional<User> findByUsername(String username);
    List<Role> getUserRoles(User user);
}
