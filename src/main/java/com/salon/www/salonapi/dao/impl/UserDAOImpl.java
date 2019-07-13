package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.UserDAO;
import com.salon.www.salonapi.mapper.UserRowMapper;
import com.salon.www.salonapi.model.Role;
import com.salon.www.salonapi.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
       return (Optional<UserDto>) jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id=?",
                new Object[] {id},
                new UserRowMapper()
        );
    }

    public List<UserDto> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users", new UserRowMapper()
        );
    }

    public void update(UserDto user) {
        jdbcTemplate.update(
                "UPDATE users SET username=?, password=? WHERE id=?",
                user.getUsername(),
                user.getPassword(),
                user.getId()
        );
    }

    public void delete(UserDto user) {
        jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                user.getId()
        );
    }

    @Override
    public void save(UserDto user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(
                            "INSERT INTO users(username, password) VALUES(?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ps.setString(1, user.getUsername());
            ps.setString( 2, user.getPassword());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return Optional.ofNullable((UserDto) jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE username=?",
                new Object[] {username},
                new UserRowMapper()
        ));

    }

    @Override
    public List<Role> getUserRoles(UserDto user) {
        return jdbcTemplate.query(
                "SELECT * FROM roles r " +
                        "INNER JOIN users_x_roles uxr on r.id = uxr.roles_id " +
                        "WHERE uxr.users_id=?",
                new Object[] {user.getId()},
                new UserRowMapper()
        );
    }
}
