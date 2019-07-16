package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Booking;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class BookingRowMapper implements RowMapper<Booking> {
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setCustomerId(rs.getLong("customers_id"));
        booking.setEmployeeId(rs.getLong("employees_id"));
        booking.setBookingTime(rs.getTimestamp("booking_time"));
        booking.setEndTime(rs.getTimestamp("end_time"));

        return booking;
    }
}
