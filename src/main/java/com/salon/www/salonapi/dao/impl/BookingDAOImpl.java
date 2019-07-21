package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.BookingDAO;
import com.salon.www.salonapi.exception.BookingCreationFailedException;
import com.salon.www.salonapi.exception.BookingDeletionFailedException;
import com.salon.www.salonapi.exception.BookingUpdateFailedException;
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
        String sql = "SELECT * FROM bookings WHERE id=?";
        return jdbcTemplate.query(
                        sql,
                        rs -> rs.next() ? Optional.ofNullable(new BookingRowMapper().mapRow(rs, 1)) : Optional.empty(),
                        id
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
        try {
            jdbcTemplate.update(
                    "INSERT INTO bookings (employees_id, customers_id, booking_time, end_time) VALUES(?,?,?,?)",
                    booking.getCustomerId(),
                    booking.getEmployeeId(),
                    booking.getBookingTime(),
                    booking.getEndTime()
            );
        } catch (Exception ex) {
            throw new BookingCreationFailedException();
        }
    }

    @Override
    public void update(Booking booking) {
        if(jdbcTemplate.update(
                "UPDATE bookings SET customers_id=?, employees_id=?, booking_time=?, end_time=? WHERE id=?",
                booking.getCustomerId(),
                booking.getEmployeeId(),
                booking.getBookingTime(),
                booking.getEndTime(),
                booking.getId()) != 1) {
            throw new BookingUpdateFailedException();
        }
    }

    @Override
    public void delete(Booking booking) {
        if(jdbcTemplate.update(
                "DELETE FROM bookings WHERE id = ?",
                booking.getId()) != 1) {
            throw new BookingDeletionFailedException();
        }

    }
}
