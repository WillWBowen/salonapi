package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.model.Role;
import com.salon.www.salonapi.model.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends Dao<UserDto> {
    Optional<UserDto> findByUsername(String username);
    List<Role> getUserRoles(UserDto user);
}
