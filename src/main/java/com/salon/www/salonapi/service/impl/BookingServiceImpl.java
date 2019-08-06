package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.BookingDAO;
import com.salon.www.salonapi.exception.BookingNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.service.itf.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
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
            if(start.before(booking.getEndTime()) && end.after(booking.getBookingTime())) return true;
        }
        return false;
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingDAO.get(bookingId).orElseThrow(BookingNotFoundException::new);
        bookingDAO.delete(booking);
    }
}
