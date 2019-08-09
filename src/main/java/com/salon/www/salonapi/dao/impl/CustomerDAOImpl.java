package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.exception.CustomerCreationFailedException;
import com.salon.www.salonapi.exception.CustomerDeletionFailedException;
import com.salon.www.salonapi.exception.CustomerUpdateFailedException;
import com.salon.www.salonapi.mapper.BookingRowMapper;
import com.salon.www.salonapi.mapper.CustomerRowMapper;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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
        String sql = "SELECT * FROM customers WHERE id=?";
        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? Optional.ofNullable(new CustomerRowMapper().mapRow(rs, 1)) : Optional.empty(),
                customerId
        );
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email=?";
        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? Optional.ofNullable(new CustomerRowMapper().mapRow(rs, 1)) : Optional.empty(),
                email
        );
    }

    @Override
    public List<Booking> getBookingsForDate(Long customerId, Timestamp date) {
        String sql = "SELECT * FROM bookings WHERE customers_id=? AND DATE(booking_time) = DATE(?)";
        return jdbcTemplate.query(
                sql,
                new Object[] {
                        customerId,
                        date
                },
                new BookingRowMapper()
        );
    }

    public void delete(Customer customer) {
        if(jdbcTemplate.update(
                "DELETE FROM customers WHERE id = ?",
                customer.getId()) != 1) {
            throw new CustomerDeletionFailedException();
        }
    }

    public void update(Customer customer) {
        if(jdbcTemplate.update(
                "UPDATE customers SET first_name=?, last_name=?, email=?, phone=? WHERE id=?",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getId()) != 1) {
            throw new CustomerUpdateFailedException();
        }
    }

    public void save(Customer customer) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO customers(users_id, first_name, last_name, email, phone) VALUES(?,?,?,?,?)",
                    customer.getUserId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhone()
            );
        } catch (Exception ex) {
            throw new CustomerCreationFailedException("Failed to create customer.");
        }
    }


}
