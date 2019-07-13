package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.BookingDAO;
import com.salon.www.salonapi.mapper.BookingRowMapper;
import com.salon.www.salonapi.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("bookingDao")
public class BookingDAOImpl implements BookingDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookingDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Booking> get(long id) {
        return (Optional<Booking>) jdbcTemplate.queryForObject(
                        "SELECT * FROM bookings WHERE id=?",
                        new Object[] {id},
                        new BookingRowMapper()
        );
    }

    @Override
    public List<Booking> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM bookings", new BookingRowMapper()
        );
    }

    @Override
    public void save(Booking booking) {
        jdbcTemplate.update(
                "INSERT INTO bookings(customers_id, employees_id, booking_time, end_time) VALUES(?,?,?,?)",
               booking.getCustomerid(),
               booking.getEmployeeid(),
               booking.getBookingTime(),
               booking.getEndTime()
               );
    }

    @Override
    public void update(Booking booking) {
        jdbcTemplate.update(
                "UPDATE bookings SET customers_id=?, employees_id=?, booking_time=?, end_time=? WHERE id=?",
                booking.getCustomerid(),
                booking.getEmployeeid(),
                booking.getBookingTime(),
                booking.getEndTime(),
                booking.getId()
                );
    }

    @Override
    public void delete(Booking booking) {
        jdbcTemplate.update(
                "DELETE FROM bookings WHERE id = ?",
                booking.getId()
        );
    }
}
