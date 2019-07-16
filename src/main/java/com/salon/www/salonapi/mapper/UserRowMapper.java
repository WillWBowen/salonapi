package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDto> {
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDto user = new UserDto();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword("REDACTED");

        return user;
    }
}
