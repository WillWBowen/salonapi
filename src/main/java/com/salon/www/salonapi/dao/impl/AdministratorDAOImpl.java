package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.AdministratorDAO;
import com.salon.www.salonapi.mapper.AdministratorRowMapper;
import com.salon.www.salonapi.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("administratorDao")
public class AdministratorDAOImpl implements AdministratorDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AdministratorDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Administrator> get(long id) {
        return (Optional<Administrator>) jdbcTemplate.queryForObject(
               "SELECT * FROM administrators WHERE id=?",
                new Object[] {id},
                new AdministratorRowMapper()

        );
    }

    @Override
    public List<Administrator> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM administrators", new AdministratorRowMapper()
        );
    }

    @Override
    public void save(Administrator administrator) {
        jdbcTemplate.update(
                "INSERT INTO administrators(first_name, last_name) VALUES(?,?)",
                        administrator.getFirstName(),
                        administrator.getLastName()
                );
    }

    @Override
    public void update(Administrator administrator) {
        jdbcTemplate.update(
                "UPDATE customers SET first_name=?, last_name=? WHERE id=?",
                        administrator.getFirstName(),
                        administrator.getLastName(),
                        administrator.getId()
                );
    }

    @Override
    public void delete(Administrator administrator) {
        jdbcTemplate.update(
                "DELETE FROM administrators WHERE id = ?",
                administrator.getId()
        );
    }
}
