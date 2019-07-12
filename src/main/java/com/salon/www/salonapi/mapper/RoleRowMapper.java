package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper {
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("role_id"));
        role.setName(rs.getString("role"));


        return role;
    }
}
