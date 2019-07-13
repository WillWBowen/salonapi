package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.CustomerDAO;
import com.salon.www.salonapi.mapper.CustomerRowMapper;
import com.salon.www.salonapi.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository("customerDao")
public class CustomerDAOImpl implements CustomerDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM customers", new CustomerRowMapper()
        );


    }

    public Optional<Customer> get(long customerId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE id=?",
                new Object[] {customerId},
                new CustomerRowMapper()
        ));
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE email=?",
                new Object[] {email},
                new CustomerRowMapper()
        ));
    }

    public void delete(Customer customer) {
        jdbcTemplate.update(
                "DELETE FROM customers WHERE id = ?",
                customer.getId()
        );
    }

    public void update(Customer customer) {
        jdbcTemplate.update(
                "UPDATE customers SET first_name=?, last_name=?, email=?, phone=? WHERE id=?",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getId()
                );
    }

    public void save(Customer customer) {
        jdbcTemplate.update(
                "INSERT INTO customers(first_name, last_name, email, phone) VALUES(?,?,?,?)",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone()
                );


    }


}
