package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.CapabilityDAO;
import com.salon.www.salonapi.exception.CapabilityCreationFailedException;
import com.salon.www.salonapi.exception.CapabilityDeletionFailedException;
import com.salon.www.salonapi.exception.CapabilityUpdateFailedException;
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
        String sql = "SELECT * FROM capabilities WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new CapabilityRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    @Override
    public List<Capability> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM capabilities", new CapabilityRowMapper()
        );
    }

    @Override
    public void save(Capability capability) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO capabilities(name) VALUES(?)",
                    capability.getName()
            );
        } catch (Exception ex) {
            throw new CapabilityCreationFailedException();
        }
    }

    @Override
    public void update(Capability capability) {
        if(jdbcTemplate.update(
                "UPDATE capabilities SET name=? WHERE id=?",
                capability.getName(),
                capability.getId()) != 1) {
            throw new CapabilityUpdateFailedException();
        }
    }

    @Override
    public void delete(Capability capability) {
        if(jdbcTemplate.update(
                "DELETE FROM capabilities WHERE id = ?",
                capability.getId()) != 1) {
            throw new CapabilityDeletionFailedException();
        }
    }
}
