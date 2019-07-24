package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.BookingDAO;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.service.itf.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Service("bookingService")
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingDAO bookingDAO;
    @Override
    public List<Booking> getBookings() {
        return bookingDAO.getAll();
    }

    public List<Booking> getBookings(Timestamp start, Timestamp end) {
        return bookingDAO.getForRange(start, end);
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return bookingDAO.get(bookingId).orElse(null);
    }

    @Override
    public void createBooking(Booking booking) {
        bookingDAO.save(booking);
    }

    public Boolean bookingTimeHasConflict(List<Booking> bookings, Timestamp start, Timestamp end) {
        for(Booking booking : bookings) {
            // booking starts in the middle of another booking
            if(start.after(booking.getBookingTime()) && !start.after(booking.getEndTime())) return true;
            // booking ends in the middle of another booking
            if(end.before(booking.getEndTime()) && !end.before(booking.getBookingTime())) return true;
            // booking starts at same time as another booking
            if(start.equals(booking.getBookingTime())) return true;
            // booking ends at the same time as another booking
            if(end.equals(booking.getEndTime())) return true;
        }

        return false;
    }
}
