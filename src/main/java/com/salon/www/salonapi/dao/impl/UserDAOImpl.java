package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.UserDAO;
import com.salon.www.salonapi.exception.UserCreationFailedException;
import com.salon.www.salonapi.exception.UserDeletionFailedException;
import com.salon.www.salonapi.exception.UserUpdateFailedException;
import com.salon.www.salonapi.mapper.AuthorityRowMapper;
import com.salon.www.salonapi.mapper.RoleRowMapper;
import com.salon.www.salonapi.mapper.UserRowMapper;
import com.salon.www.salonapi.model.Role;
import com.salon.www.salonapi.model.security.Authority;
import com.salon.www.salonapi.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userDao")
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> get(long id) {
        String sql = "SELECT * FROM users WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new UserRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users", new UserRowMapper()
        );
    }

    public void update(User user) {
        if(jdbcTemplate.update(
                "UPDATE users SET username=?, password=? WHERE id=?",
                user.getUsername(),
                user.getPassword(),
                user.getId()) != 1) {
            throw new UserUpdateFailedException();
        }
    }

    public void delete(User user) {
        if(jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                user.getId())!= 1) {
            throw new UserDeletionFailedException();
        }
    }

    @Override
    public void save(User user) {
        try {
            jdbcTemplate.update(
                "INSERT INTO users(username, password, email, enabled) VALUES(?,?,?,?)",
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getEnabled()
            );
        } catch(Exception ex) {
            throw new UserCreationFailedException();
        }

    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new UserRowMapper().mapRow(rs, 1)) : Optional.empty(),
                username);
    }

    @Override
    public List<Role> getUserRoles(User user) {
        return jdbcTemplate.query(
                "SELECT * FROM roles r " +
                        "INNER JOIN users_x_roles uxr on r.id = uxr.roles_id " +
                        "WHERE uxr.users_id=?",
                new Object[] {user.getId()},
                new RoleRowMapper()
        );
    }

    @Override
    public List<Authority> getCapabilitiesForUser(User user) {
        String sql = "SELECT id, name FROM authorities a " +
                     "INNER JOIN(" +
                         "SELECT authorities_id FROM roles_x_authorities rxa " +
                         "INNER JOIN (" +
                             "SELECT id as role_id FROM roles r " +
                             "INNER JOIN users_x_roles uxr ON uxr.users_id = ?" +
                         ") ri on ri.role_id = rxa.roles_id" +
                      ") ai on ai.authorities_id = a.id";
        return jdbcTemplate.query(
                sql,
                new Object[] {user.getId()},
                new AuthorityRowMapper()
        );
    }
}
