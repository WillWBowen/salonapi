package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.BookingDAO;
import com.salon.www.salonapi.mapper.BookingRowMapper;
import com.salon.www.salonapi.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class BookingDAOImpl implements BookingDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Booking> get(long id) {
        Optional<Booking> booking =
                (Optional<Booking>) jdbcTemplate.queryForObject(
                        "SELECT * FROM bookings WHERE id=?",
                        new Object[] {id},
                        new BookingRowMapper()

                );

        return booking;
    }

    @Override
    public List<Booking> getAll() {
        List<Booking> bookings = jdbcTemplate.query(
                "SELECT * FROM bookings", new BookingRowMapper()
        );

        return bookings;
    }

    @Override
    public void save(Booking booking) {
        jdbcTemplate.update(
                "INSERT INTO bookings(customers_id, employees_id, booking_time, end_time) VALUES(?,?,?,?)",
                new Object[] {
                        booking.getCustomerid(),
                        booking.getEmployeeid(),
                        booking.getBookingTime(),
                        booking.getEndTime()
                });
    }

    @Override
    public void update(Booking booking, String[] params) {
        jdbcTemplate.update(
                "UPDATE bookings SET customers_id=?, employees_id=?, booking_time=?, end_time=? WHERE id=?",
                new Object[] {
                        params[0],
                        params[1],
                        params[2],
                        params[3],
                        booking.getId()
                });
    }

    @Override
    public void delete(Booking booking) {
        jdbcTemplate.update(
                "DELETE FROM bookings WHERE id = ?",
                new Object[] {booking.getId()}
        );
    }
}
