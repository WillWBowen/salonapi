package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.CapabilityDAO;
import com.salon.www.salonapi.mapper.CapabilityRowMapper;
import com.salon.www.salonapi.model.Capability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class CapabilityDAOImpl implements CapabilityDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Capability> get(long id) {
        Optional<Capability> capability =
                (Optional<Capability>) jdbcTemplate.queryForObject(
                        "SELECT * FROM capabilities WHERE id=?",
                        new Object[] {id},
                        new CapabilityRowMapper()

                );

        return capability;
    }

    @Override
    public List<Capability> getAll() {
        List<Capability> capabilities = jdbcTemplate.query(
                "SELECT * FROM capabilities", new CapabilityRowMapper()
        );

        return capabilities;
    }

    @Override
    public void save(Capability capability) {
        jdbcTemplate.update(
                "INSERT INTO capabilities(name) VALUES(?)",
                new Object[] {
                        capability.getName()
                });
    }

    @Override
    public void update(Capability capability, String[] params) {
        jdbcTemplate.update(
                "UPDATE capabilities SET name=? WHERE id=?",
                new Object[] {
                        params[0],
                        capability.getId()
                });
    }

    @Override
    public void delete(Capability capability) {
        jdbcTemplate.update(
                "DELETE FROM capabilities WHERE id = ?",
                new Object[] {capability.getId()}
        );
    }
}
