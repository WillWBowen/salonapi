package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Skill;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillRowMapper implements RowMapper<Skill> {
    public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
        Skill skill = new Skill();
        skill.setId(rs.getLong("id"));
        skill.setName(rs.getString("name"));
        skill.setPrice(rs.getInt("price"));

        return skill;
    }
}
