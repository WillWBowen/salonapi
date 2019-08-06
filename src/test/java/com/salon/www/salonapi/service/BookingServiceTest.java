package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.BookingDAO;
import com.salon.www.salonapi.exception.BookingNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.service.impl.BookingServiceImpl;
import com.salon.www.salonapi.service.itf.BookingService;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@Log4j2
@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    private BookingDAO bookingDao;

    private static final Timestamp START_1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 0).getTimeInMillis());
    private static final Timestamp END_1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 12, 30).getTimeInMillis());


    @Before
    public void setUp() {
        this.bookingService = new BookingServiceImpl(bookingDao);
    }

    @Test
    public void getBooking_shouldReturnBooking_ifPresent() {
        Booking booking = new Booking(10L, 5L, 12L, null, null, null);
        given(bookingDao.get(anyLong())).willReturn(Optional.of(booking));

        Booking returnedBooking = bookingService.getBooking(10L);

        then(bookingDao).should(times(1)).get(10L);
        assertThat(returnedBooking).isEqualTo(booking);
    }

    @Test
    public void getBooking_shouldReturnNull_ifNotPresent() {
        given(bookingDao.get(anyLong())).willReturn(Optional.empty());

        Booking returnedBooking = bookingService.getBooking(10L);

        then(bookingDao).should(times(1)).get(10L);
        assertThat(returnedBooking).isNull();
    }

    @Test
    public void getBookings_shouldReturnListOfBookings_ifPresent() {
        Booking first = new Booking(10L, 5L, 12L, null, null, null);
        Booking second =new Booking(14L,10L, 12L, null, null, null);
        given(bookingDao.getAll()).willReturn(Arrays.asList(first, second));

        List<Booking> bookings = bookingService.getBookings();

        then(bookingDao).should(times(1)).getAll();
        assertThat(bookings).contains(first);
        assertThat(bookings).contains(second);
    }

    @Test
    public void getBookings_shouldReturnNull_ifNoBookingsPresent() {
        given(bookingDao.getAll()).willReturn(null);

        List<Booking> bookings = bookingService.getBookings();

        then(bookingDao).should(times(1)).getAll();
        assertThat(bookings).isNullOrEmpty();
    }

    @Test
    public void getBookings_withRangeParameters_shouldReturnListOfBookings_ifPresent() {
        Booking first = new Booking(10L, 5L, 12L, null, null, null);
        Booking second =new Booking(14L,10L, 12L, null, null, null);
        given(bookingDao.getForRange(any(), any())).willReturn(Arrays.asList(first, second));

        List<Booking> bookings = bookingService.getBookings(START_1, END_1);

        then(bookingDao).should(times(1)).getForRange(START_1, END_1);
        assertThat(bookings).contains(first);
        assertThat(bookings).contains(second);
    }

    @Test
    public void getBookings_withRangeParameters_shouldReturnNull_ifNoBookingsPresent() {
        given(bookingDao.getForRange(any(), any())).willReturn(null);

        List<Booking> bookings = bookingService.getBookings(START_1, END_1);

        then(bookingDao).should(times(1)).getForRange(START_1, END_1);
        assertThat(bookings).isNullOrEmpty();
    }

    @Test
    public void createCustomer_callsDAOSaveMethod() {
        Booking booking = new Booking(10L, 5L, 12L, null, null, null);
        doNothing().when(bookingDao).save(any());

        bookingService.createBooking(booking);
        then(bookingDao).should(times(1)).save(booking);
    }

    @Test
    public void bookingTimeHasConflict_TestAllDecisionTreeBranches() {
        Booking booking = new Booking(10L, 5L, 12L, START_1, END_1, null);
        // set start to after booking start, before booking end, set end to valid time
        Timestamp start = new Timestamp(END_1.getTime() - 1000 * 60 * 5);
        Timestamp end = new Timestamp(END_1.getTime()  + 1000 * 60 * 25);
        List<Booking> bookings = Arrays.asList(booking);

        log.info("Start time  : " + start);
        log.info("Booking time: " + booking.getBookingTime());
        log.info("End time    : " + end);
        log.info("Booking end : " + booking.getEndTime());
        assertThat(bookingService.bookingTimeHasConflict(bookings, start, end)).isTrue();
        // set start equal to booking end time
        start.setTime(END_1.getTime());
        assertThat(bookingService.bookingTimeHasConflict(bookings, start, end)).isFalse();
        // set start equal to after end time
        start.setTime(start.getTime() + 1000 * 60 * 5);
        assertThat(bookingService.bookingTimeHasConflict(bookings, start, end)).isFalse();
        // now move start time to before booking time, but leave end after booking end time
        start.setTime(START_1.getTime() - 1000 * 60 * 5);
        assertThat(bookingService.bookingTimeHasConflict(bookings, start, end)).isTrue();
        // set end to within  booking time window
        end.setTime(START_1.getTime() + 100 * 60 * 5);
        assertThat(bookingService.bookingTimeHasConflict(bookings, start, end)).isTrue();
        // send end equal to start time
        end.setTime(START_1.getTime());
        assertThat(bookingService.bookingTimeHasConflict(bookings, start, end)).isFalse();
    }

    @Test
    public void deleteBooking_ShouldCallDAOdelete_ifBookingExists() {
        Booking booking = new Booking(10L, 5L, 12L, null, null, null);
        given(bookingDao.get(anyLong())).willReturn(Optional.of(booking));
        doNothing().when(bookingDao).delete(any());

        bookingService.deleteBooking(10L);
        then(bookingDao).should(times(1)).delete(booking);
    }

    @Test(expected = BookingNotFoundException.class)
    public void deleteBooking_ShouldThrowError_ifBookingDoesNotExist() {
        Booking booking = new Booking(10L, 5L, 12L, null, null, null);
        given(bookingDao.get(anyLong())).willReturn(Optional.empty());

        bookingService.deleteBooking(10L);
    }
}
