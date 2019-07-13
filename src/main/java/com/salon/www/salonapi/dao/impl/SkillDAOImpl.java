package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.mapper.SkillRowMapper;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
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
        return (Optional<Skill>) jdbcTemplate.queryForObject(
                "SELECT * FROM skills WHERE id=?",
                new Object[] {id},
                new SkillRowMapper()
        );
    }

    @Override
    public List<Skill> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM skills", new SkillRowMapper()
        );
    }

    @Override
    public void save(Skill skill) {
        jdbcTemplate.update(
                "INSERT INTO skills(name, price) VALUES(?,?)",
                skill.getName(),
                skill.getPrice()
        );
    }

    @Override
    public void update(Skill skill) {
        jdbcTemplate.update(
                "UPDATE skills SET name=?, price=? WHERE id=?",
                skill.getName(),
                skill.getPrice(),
                skill.getId()

        );
    }

    @Override
    public void delete(Skill skill) {
        jdbcTemplate.update(
                "DELETE FROM skills WHERE id = ?",
                skill.getId()
        );
    }

    @Override
    public List<Skill> getForEmployee(Employee employee) {
        return jdbcTemplate.query(
                "SELECT * FROM skills s " +
                        "INNER JOIN employees_x_skills exs " +
                        "ON (s.id = exs.skills_id) " +
                        "WHERE exs.employees_id=?",
                new Object[] {
                        employee.getId()
                },
                new SkillRowMapper()
        );
    }
}
