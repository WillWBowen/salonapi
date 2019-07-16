package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.AdministratorDAO;
import com.salon.www.salonapi.exception.AdministratorCreationFailedException;
import com.salon.www.salonapi.exception.AdministratorDeletionFailedException;
import com.salon.www.salonapi.exception.AdministratorUpdateFailedException;
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
        String sql = "SELECT * FROM administrators WHERE id=?";
        return jdbcTemplate.query(sql,
                rs -> rs.next() ? Optional.ofNullable(new AdministratorRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    @Override
    public List<Administrator> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM administrators", new AdministratorRowMapper()
        );
    }

    @Override
    public void save(Administrator administrator) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO administrators(users_id, first_name, last_name) VALUES(?,?,?)",
                    administrator.getUserId(),
                    administrator.getFirstName(),
                    administrator.getLastName()
            );
        } catch (Exception ex) {
            throw new AdministratorCreationFailedException();
        }
    }

    @Override
    public void update(Administrator administrator) {
        if(jdbcTemplate.update(
                "UPDATE administrators SET first_name=?, last_name=? WHERE id=?",
                        administrator.getFirstName(),
                        administrator.getLastName(),
                        administrator.getId()) != 1) {
            throw new AdministratorUpdateFailedException();
        }
    }

    @Override
    public void delete(Administrator administrator) {
        if(jdbcTemplate.update(
                "DELETE FROM administrators WHERE id = ?",
                administrator.getId()) != 1) {
            throw new AdministratorDeletionFailedException();
        }
    }
}
