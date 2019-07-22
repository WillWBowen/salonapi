package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.SkillCreationFailedException;
import com.salon.www.salonapi.exception.SkillDeletionFailedException;
import com.salon.www.salonapi.exception.SkillUpdateFailedException;
import com.salon.www.salonapi.mapper.SkillRowMapper;
import com.salon.www.salonapi.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("skillDao")
public class SkillDAOImpl implements SkillDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SkillDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Skill> get(long id) {
        String sql = "SELECT * FROM skills WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new SkillRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    @Override
    public List<Skill> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM skills",
                new SkillRowMapper()
        );
    }

    @Override
    public void save(Skill skill) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO skills(name, price) VALUES(?,?)",
                    skill.getName(),
                    skill.getPrice());
        }catch (Exception ex) {
            throw new SkillCreationFailedException();
        }
    }

    @Override
    public void update(Skill skill) throws DataAccessException {
        int rs = jdbcTemplate.update(
            "UPDATE skills SET name=?, price=? WHERE id=?",
            skill.getName(),
            skill.getPrice(),
            skill.getId());
        if(rs != 1){
            throw new SkillUpdateFailedException();
        }
    }

    @Override
    public void delete(Skill skill) {
        if (jdbcTemplate.update(
                "DELETE FROM skills WHERE id = ?",
                skill.getId()) != 1) {
            throw new SkillDeletionFailedException();
        }

    }

    @Override
    public List<Skill> getForEmployee(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM skills s " +
                        "INNER JOIN employees_x_skills exs " +
                        "ON (s.id = exs.skills_id) " +
                        "WHERE exs.employees_id=?",
                new Object[]{
                        id
                },
                new SkillRowMapper()
        );
    }
}
