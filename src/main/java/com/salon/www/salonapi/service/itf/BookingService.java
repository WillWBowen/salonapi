package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingService {
    List<Booking> getBookings();
    List<Booking> getBookings(Timestamp start, Timestamp end);

    Booking getBooking(Long bookingId);

    void createBooking(Booking booking);

    Boolean bookingTimeHasConflict(List<Booking> bookings, Timestamp start, Timestamp end);
}
