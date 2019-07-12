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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Skill> get(long id) {
        Optional<Skill> skill = (Optional<Skill>) jdbcTemplate.queryForObject(
                "SELECT * FROM skills WHERE id=?",
                new Object[] {id},
                new SkillRowMapper()
        );
        return skill;
    }

    @Override
    public List<Skill> getAll() {
        List<Skill> skills = jdbcTemplate.query(
                "SELECT * FROM skills", new SkillRowMapper()
        );

        return skills;
    }

    @Override
    public void save(Skill skill) {
        jdbcTemplate.update(
                "INSERT INTO skills(name, price) VALUES(?,?)",
                new Object[] {
                        skill.getName(),
                        skill.getPrice()
                });
    }

    @Override
    public void update(Skill skill) {
        jdbcTemplate.update(
                "UPDATE skills SET name=?, price=? WHERE id=?",
                new Object[] {
                        skill.getName(),
                        skill.getPrice(),
                        skill.getId()
                }
        );
    }

    @Override
    public void delete(Skill skill) {
        jdbcTemplate.update(
                "DELETE FROM skills WHERE id = ?",
                new Object[] {skill.getId()}
        );
    }

    @Override
    public List<Skill> getForEmployee(Employee employee) {
        List<Skill> skills = jdbcTemplate.query(
                "SELECT * FROM skills s " +
                        "INNER JOIN employees_x_skills exs " +
                        "ON (s.id = exs.id) " +
                        "WHERE exs.id=?",
                new Object[] {
                        employee.getId()
                },
                new SkillRowMapper()
        );

        return skills;
    }
}
