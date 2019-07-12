package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Service;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceRowMapper implements RowMapper {
    public Service mapRow(ResultSet rs, int rowNum) throws SQLException {
        Service service = new Service();
        service.setId(rs.getLong("id"));
        service.setName(rs.getString("name"));
        service.setPrice(rs.getInt("price"));

        return service;
    }
}
