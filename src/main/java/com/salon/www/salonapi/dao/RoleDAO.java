package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.model.Role;

import java.util.Optional;

public interface RoleDAO extends Dao<Role>{
    Optional<Role> findByRole(String role);
}
