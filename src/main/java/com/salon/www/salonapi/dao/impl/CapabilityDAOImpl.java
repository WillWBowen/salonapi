package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.CapabilityDAO;
import com.salon.www.salonapi.mapper.CapabilityRowMapper;
import com.salon.www.salonapi.model.Capability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("capabilityDao")
public class CapabilityDAOImpl implements CapabilityDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CapabilityDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Capability> get(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                        "SELECT * FROM capabilities WHERE id=?",
                        new Object[] {id},
                        new CapabilityRowMapper()
        ));
    }

    @Override
    public List<Capability> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM capabilities", new CapabilityRowMapper()
        );
    }

    @Override
    public void save(Capability capability) {
        jdbcTemplate.update(
                "INSERT INTO capabilities(name) VALUES(?)",
                capability.getName()
                );
    }

    @Override
    public void update(Capability capability) {
        jdbcTemplate.update(
                "UPDATE capabilities SET name=? WHERE id=?",
                capability.getName(),
                capability.getId()
                );
    }

    @Override
    public void delete(Capability capability) {
        jdbcTemplate.update(
                "DELETE FROM capabilities WHERE id = ?",
                capability.getId()
        );
    }
}
