package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.ServiceDAO;
import com.salon.www.salonapi.mapper.ServiceRowMapper;
import com.salon.www.salonapi.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class ServiceDAOImpl implements ServiceDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Service> get(long id) {
        Optional<Service> service = (Optional<Service>) jdbcTemplate.queryForObject(
                "SELECT * FROM services WHERE id=?",
                new Object[] {id},
                new ServiceRowMapper()
        );
        return service;
    }

    @Override
    public List<Service> getAll() {
        List<Service> services = jdbcTemplate.query(
                "SELECT * FROM services", new ServiceRowMapper()
        );

        return services;
    }

    @Override
    public void save(Service service) {
        jdbcTemplate.update(
                "INSERT INTO services(name, price) VALUES(?,?)",
                new Object[] {
                        service.getName(),
                        service.getPrice()
                });
    }

    @Override
    public void update(Service service, String[] params) {
        jdbcTemplate.update(
                "UPDATE services SET name=?, price=? WHERE id=?",
                new Object[] {
                        params[0],
                        params[1],
                        service.getId()
                }
        );
    }

    @Override
    public void delete(Service service) {
        jdbcTemplate.update(
                "DELETE FROM services WHERE id = ?",
                new Object[] {service.getId()}
        );
    }
}
