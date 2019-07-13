package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.RoleDAO;
import com.salon.www.salonapi.mapper.RoleRowMapper;
import com.salon.www.salonapi.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("roleDao")
public class RoleDAOImpl implements RoleDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Role> findByRole(String roleName) {
        return (Optional<Role>) jdbcTemplate.queryForObject(
                "SELECT * FROM roles WHERE name=?",
                new Object[] {roleName},
                new RoleRowMapper()
        );
    }

    @Override
    public Optional<Role> get(long id) {
        return (Optional<Role>) jdbcTemplate.queryForObject(
                        "SELECT * FROM roles WHERE id=?",
                        new Object[] {id},
                        new RoleRowMapper()

                );
    }

    @Override
    public List<Role> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM roles", new RoleRowMapper()
        );
    }

    @Override
    public void save(Role role) {
        jdbcTemplate.update(
                "INSERT INTO roles(name) VALUES(?)",
                role.getName()
        );
    }

    @Override
    public void update(Role role) {
        jdbcTemplate.update(
                "UPDATE roles SET name=? WHERE id=?",
                role.getName(),
                role.getId()
        );
    }

    @Override
    public void delete(Role role) {
        jdbcTemplate.update(
                "DELETE FROM roles WHERE id = ?",
                role.getId()
        );
    }
}
