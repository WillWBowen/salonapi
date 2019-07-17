package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.UserDAO;
import com.salon.www.salonapi.exception.UserCreationFailedException;
import com.salon.www.salonapi.exception.UserDeletionFailedException;
import com.salon.www.salonapi.exception.UserUpdateFailedException;
import com.salon.www.salonapi.mapper.RoleRowMapper;
import com.salon.www.salonapi.mapper.UserRowMapper;
import com.salon.www.salonapi.model.Role;
import com.salon.www.salonapi.model.UserDto;
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
    public Optional<UserDto> get(long id) {
        String sql = "SELECT * FROM users WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new UserRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    public List<UserDto> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users", new UserRowMapper()
        );
    }

    public void update(UserDto user) {
        if(jdbcTemplate.update(
                "UPDATE users SET username=?, password=? WHERE id=?",
                user.getUsername(),
                user.getPassword(),
                user.getId()) != 1) {
            throw new UserUpdateFailedException();
        }
    }

    public void delete(UserDto user) {
        if(jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                user.getId())!= 1) {
            throw new UserDeletionFailedException();
        }
    }

    @Override
    public void save(UserDto user) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO users(username, password) VALUES(?,?)",
                    user.getUsername(),
                    user.getPassword());
        } catch(Exception ex) {
            throw new UserCreationFailedException();
        }

    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new UserRowMapper().mapRow(rs, 1)) : Optional.empty(),
                username);
    }

    @Override
    public List<Role> getUserRoles(UserDto user) {
        return jdbcTemplate.query(
                "SELECT * FROM roles r " +
                        "INNER JOIN users_x_roles uxr on r.id = uxr.roles_id " +
                        "WHERE uxr.users_id=?",
                new Object[] {user.getId()},
                new RoleRowMapper()
        );
    }
}
