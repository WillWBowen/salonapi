package com.salon.www.salonapi.mapper;


import com.salon.www.salonapi.model.security.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setLastPasswordResetDate(rs.getDate("lastPasswordResetDate"));

        return user;
    }
}
