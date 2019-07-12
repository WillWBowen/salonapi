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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Customer> getAll() {
        List<Customer> customers = jdbcTemplate.query(
                "SELECT * FROM customers", new CustomerRowMapper()
        );

        return customers;
    }

    public Optional<Customer> get(long customerId) {
        Optional<Customer> customer = (Optional<Customer>) jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE customer_id=?",
                new Object[] {customerId},
                new CustomerRowMapper()
        );
        return customer;
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        Optional<Customer> customer = (Optional<Customer>) jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE email=?",
                new Object[] {email},
                new CustomerRowMapper()
        );
        return customer;
    }

    public void delete(Customer customer) {
        jdbcTemplate.update(
                "DELETE FROM customers WHERE customer_id = ?",
                new Object[] {customer.getId()}
        );
    }

    public void update(Customer customer, String[] params) {
        jdbcTemplate.update(
                "UPDATE customers SET first_name=?, last_name=?, email=? WHERE customer_id=?",
                new Object[] {
                        params[0],
                        params[1],
                        params[2],
                        customer.getId()
                });
    }

    public void save(Customer customer) {
        jdbcTemplate.update(
                "INSERT INTO customers(first_name, last_name, email) VALUES(?,?,?)",
                new Object[] {
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail()
                });


    }


}
