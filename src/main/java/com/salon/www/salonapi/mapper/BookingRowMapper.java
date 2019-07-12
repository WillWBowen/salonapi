package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.Booking;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingRowMapper implements RowMapper {
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setCustomerid(rs.getLong("customers_id"));
        booking.setEmployeeid(rs.getLong("employees_id"));
        booking.setBookingTime(rs.getTime("booking_time"));

        return booking;
    }
}
