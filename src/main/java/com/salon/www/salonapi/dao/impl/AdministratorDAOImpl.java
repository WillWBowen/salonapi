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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Administrator> get(long id) {
        Optional<Administrator> administrator =
                (Optional<Administrator>) jdbcTemplate.queryForObject(
                        "SELECT * FROM administrators WHERE id=?",
                        new Object[] {id},
                        new AdministratorRowMapper()

        );

        return administrator;
    }

    @Override
    public List<Administrator> getAll() {
        List<Administrator> administrators = jdbcTemplate.query(
                "SELECT * FROM administrators", new AdministratorRowMapper()
        );

        return administrators;
    }

    @Override
    public void save(Administrator administrator) {
        jdbcTemplate.update(
                "INSERT INTO administrators(first_name, last_name) VALUES(?,?)",
                new Object[] {
                        administrator.getFirstName(),
                        administrator.getLastName()
                });
    }

    @Override
    public void update(Administrator administrator, String[] params) {
        jdbcTemplate.update(
                "UPDATE customers SET first_name=?, last_name=? WHERE id=?",
                new Object[] {
                        params[0],
                        params[1],
                        administrator.getId()
                });
    }

    @Override
    public void delete(Administrator administrator) {
        jdbcTemplate.update(
                "DELETE FROM administrators WHERE id = ?",
                new Object[] {administrator.getId()}
        );
    }
}
