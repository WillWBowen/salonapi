package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.security.Authority;
import com.salon.www.salonapi.model.security.AuthorityName;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorityRowMapper implements RowMapper<Authority> {
    public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
        Authority authority = new Authority();
        authority.setId(rs.getLong("id"));
        authority.setName(AuthorityName.valueOf(rs.getString("name")));

        return authority;
    }
}
