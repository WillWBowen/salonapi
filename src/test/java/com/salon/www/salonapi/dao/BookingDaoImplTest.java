package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.dao.itf.BookingDAO;
import com.salon.www.salonapi.exception.BookingCreationFailedException;
import com.salon.www.salonapi.exception.BookingDeletionFailedException;
import com.salon.www.salonapi.exception.BookingUpdateFailedException;
import com.salon.www.salonapi.model.Booking;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookingDaoImplTest {

    private static final String CREATE_BOOKING_T_SQL_SCRIPT = "scripts/create/bookings_t.sql";
    private static final String DROP_BOOKING_T_SQL_SCRIPT = "scripts/drop/bookings_t.sql";
    private static final String POPULATE_ONE_EMPLOYEE_ONE_CUSTOMER_T_SQL_SCRIPT = "scripts/populate/one_employee_one_customer_t.sql";
    private static final String POPULATE_ONE_BOOKING_T_SQL_SCRIPT = "scripts/populate/one_booking_t.sql";
    private static final String POPULATE_TWO_BOOKINGS_T_SQL_SCRIPT = "scripts/populate/two_bookings_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BookingDAO bookingDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_BOOKING_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_BOOKING_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddBookingToDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_ONE_CUSTOMER_T_SQL_SCRIPT));
        connection.close();
        Timestamp start = new Timestamp(new GregorianCalendar(2019, 7, 30, 11, 00).getTimeInMillis());
        Timestamp end = new Timestamp(new GregorianCalendar(2019, 7, 30, 12, 30).getTimeInMillis());
        Booking booking = new Booking(1L, 1L, start, end);
        bookingDao.save(booking);

        Optional<Booking> validBooking = bookingDao.get(1L);

        assertThat(validBooking.isPresent()).isEqualTo(true);
        assertThat(validBooking.get().getEmployeeId()).isEqualTo(booking.getEmployeeId());
        assertThat(validBooking.get().getCustomerId()).isEqualTo(booking.getCustomerId());
        assertThat(validBooking.get().getBookingTime()).isEqualTo(booking.getBookingTime());
        assertThat(validBooking.get().getEndTime()).isEqualTo((booking.getEndTime()));

    }

    @Test(expected = BookingCreationFailedException.class)
    public void save_shouldThrowError_forInvalidBookingObject() {
        Booking booking = new Booking();

        bookingDao.save(booking);
    }

    @Test
    public void get_shouldReturnValidBooking_forExistingBooking() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_BOOKING_T_SQL_SCRIPT));
        connection.close();
        Optional<Booking> validBooking = bookingDao.get(1L);

        assertThat(validBooking.isPresent()).isEqualTo(true);
        assertThat(validBooking.get().getEmployeeId()).isEqualTo(1L);
        assertThat(validBooking.get().getCustomerId()).isEqualTo(1L);
        assertThat(validBooking.get().getBookingTime()).isEqualTo(new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 00).getTimeInMillis()));
        assertThat(validBooking.get().getEndTime()).isEqualTo(new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 12, 30).getTimeInMillis()));
    }

    @Test
    public void get_shouldReturnInvalidBooking_forEmptyDatabase() {
        Optional<Booking> invalid = bookingDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Booking> noBookings = bookingDao.getAll();

        assertThat(noBookings).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfBookings_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_BOOKINGS_T_SQL_SCRIPT));

        List<Booking> bookings = bookingDao.getAll();

        Timestamp start1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 00).getTimeInMillis());
        Timestamp end1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 12, 30).getTimeInMillis());
        Timestamp start2 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 10, 00).getTimeInMillis());
        Timestamp end2 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 11, 00).getTimeInMillis());

        assertThat(bookings).isNotNull().hasSize(2);
        assertThat(bookings.contains(new Booking(1L,1L,1L, start1, end1))).isTrue();
        assertThat(bookings.contains(new Booking(2L, 1L,1L, start2, end2))).isTrue();

    }

    @Test(expected = BookingUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingBooking() {
        Booking notFound = new Booking();
        notFound.setId(new Random().nextLong());

        bookingDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingBooking() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_BOOKING_T_SQL_SCRIPT));
        connection.close();

        Timestamp start2 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 10, 00).getTimeInMillis());
        Timestamp end2 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 11, 00).getTimeInMillis());
        bookingDao.update(new Booking(1L, 1L,1L, start2, end2));

        Optional<Booking> updatedBooking = bookingDao.get(1L);
        assertThat(updatedBooking.isPresent()).isEqualTo(true);
        assertThat(updatedBooking.get().getEmployeeId()).isEqualTo(1L);
    }

    @Test
    public void delete_shouldRemoveBookingFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_BOOKING_T_SQL_SCRIPT));
        connection.close();
        Booking booking = new Booking();
        booking.setId(1L);
        bookingDao.delete(booking);

        assertThat(bookingDao.get(1L).isPresent()).isFalse();
        assertThat(bookingDao.getAll()).hasSize(0);
    }

    @Test(expected = BookingDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Booking booking = new Booking();
        booking.setId(new Random().nextLong());

        bookingDao.delete(booking);
    }
}
