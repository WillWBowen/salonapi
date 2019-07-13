package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Capability;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CapabilityRowMapper implements RowMapper<Capability> {
    public Capability mapRow(ResultSet rs, int rowNum) throws SQLException {
        Capability capability = new Capability();
        capability.setId(rs.getLong("id"));
        capability.setName(rs.getString("name"));

        return capability;
    }
}
