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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Role> findByRole(String roleName) {
        Optional<Role> role = (Optional<Role>) jdbcTemplate.queryForObject(
                "SELECT * FROM roles WHERE role=?",
                new Object[] {roleName},
                new RoleRowMapper()
        );
        return role;
    }

    @Override
    public Optional<Role> get(long id) {
        Optional<Role> role =
                (Optional<Role>) jdbcTemplate.queryForObject(
                        "SELECT * FROM roles WHERE id=?",
                        new Object[] {id},
                        new RoleRowMapper()

                );

        return role;
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = jdbcTemplate.query(
                "SELECT * FROM roles", new RoleRowMapper()
        );

        return roles;
    }

    @Override
    public void save(Role role) {
        jdbcTemplate.update(
                "INSERT INTO roles(name) VALUES(?)",
                new Object[] {
                        role.getName()
                });
    }

    @Override
    public void update(Role role) {
        jdbcTemplate.update(
                "UPDATE roles SET name=? WHERE id=?",
                new Object[] {
                        role.getName(),
                        role.getId()
                }
        );
    }

    @Override
    public void delete(Role role) {
        jdbcTemplate.update(
                "DELETE FROM roles WHERE id = ?",
                new Object[] {role.getId()}
        );
    }
}
