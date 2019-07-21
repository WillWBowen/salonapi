package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.AuthorityDAO;
import com.salon.www.salonapi.exception.AuthorityCreationFailedException;
import com.salon.www.salonapi.exception.AuthorityDeletionFailedException;
import com.salon.www.salonapi.exception.AuthorityUpdateFailedException;
import com.salon.www.salonapi.mapper.AuthorityRowMapper;
import com.salon.www.salonapi.model.security.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("authorityDao")
public class AuthorityDAOImpl implements AuthorityDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorityDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Authority> get(long id) {
        String sql = "SELECT * FROM authorities WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new AuthorityRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    @Override
    public List<Authority> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM authorities", new AuthorityRowMapper()
        );
    }

    @Override
    public void save(Authority authority) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO authorities(name) VALUES(?)",
                    authority.getName().name()
            );
        } catch (Exception ex) {
            throw new AuthorityCreationFailedException();
        }
    }

    @Override
    public void update(Authority authority) {
        try {
            jdbcTemplate.update(
                "UPDATE authorities SET name=? WHERE id=?",
                authority.getName().name(),
                authority.getId());
        } catch (Exception ex) {
            throw new AuthorityUpdateFailedException();
        }
    }

    @Override
    public void delete(Authority authority) {
        if(jdbcTemplate.update(
                "DELETE FROM authorities WHERE id = ?",
                authority.getId()) != 1) {
            throw new AuthorityDeletionFailedException();
        }
    }
}
