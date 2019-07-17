package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.RoleDAO;
import com.salon.www.salonapi.exception.RoleCreationFailedException;
import com.salon.www.salonapi.exception.RoleDeletionFailedException;
import com.salon.www.salonapi.exception.RoleUpdateFailedException;
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
        String sql = "SELECT * FROM roles WHERE name=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new RoleRowMapper().mapRow(rs, 1)) : Optional.empty(),
                roleName);
    }

    @Override
    public Optional<Role> get(long id) {
        String sql = "SELECT * FROM roles WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new RoleRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    @Override
    public List<Role> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM roles", new RoleRowMapper()
        );
    }

    @Override
    public void save(Role role) throws RoleCreationFailedException {
        try {
            jdbcTemplate.update(
                    "INSERT INTO roles(name) VALUES(?)",
                    role.getRole()
            );
        } catch(Exception ex) {
            throw new RoleCreationFailedException();
        }
    }

    @Override
    public void update(Role role) throws RuntimeException {
        if (jdbcTemplate.update(
                "UPDATE roles SET name=? WHERE id=?",
                role.getRole(),
                role.getId()) != 1) {
            throw new RoleUpdateFailedException();
        }
    }

    @Override
    public void delete(Role role) {
        if (jdbcTemplate.update(
                "DELETE FROM roles WHERE id = ?",
                role.getId()) != 1) {
            throw new RoleDeletionFailedException();
        }
    }
}
