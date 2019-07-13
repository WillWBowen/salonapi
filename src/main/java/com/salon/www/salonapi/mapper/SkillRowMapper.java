package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Skill;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillRowMapper implements RowMapper<Skill> {
    public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Skill(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getInt("price"));
    }
}
