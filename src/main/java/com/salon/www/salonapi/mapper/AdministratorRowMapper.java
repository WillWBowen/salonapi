package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Administrator;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorRowMapper implements RowMapper<Administrator> {
    public Administrator mapRow(ResultSet rs, int rowNum) throws SQLException {
        Administrator administrator = new Administrator();
        administrator.setId(rs.getLong("id"));
        administrator.setFirstName(rs.getString("first_name"));
        administrator.setLastName(rs.getString("last_name"));

        return administrator;
    }
}
