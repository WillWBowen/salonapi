package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.exception.CustomerCreationFailedException;
import com.salon.www.salonapi.exception.CustomerNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.service.impl.CustomerServiceImpl;
import com.salon.www.salonapi.service.itf.BookingService;
import com.salon.www.salonapi.service.itf.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private BookingService bookingService;
    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    @Spy
    private CustomerServiceImpl spyService;

    private static final Timestamp START_1 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 11, 0).getTimeInMillis());
    private static final Timestamp END_1 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 12, 0).getTimeInMillis());
    private static final Timestamp START_2 = new Timestamp(new GregorianCalendar(2019, Calendar.SEPTEMBER, 30, 9, 30).getTimeInMillis());
    private static final Timestamp END_2 = new Timestamp(new GregorianCalendar(2019, Calendar.SEPTEMBER, 30, 11, 15).getTimeInMillis());


    @Before
    public void setUp() {
        this.customerService = new CustomerServiceImpl(customerDAO, bookingService);
    }

    @Test
    public void getCustomer_shouldReturnCustomer_ifPresent() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        given(customerDAO.get(anyLong())).willReturn(Optional.of(customer));

        Customer returnedCustomer = customerService.getCustomer(10L);

        then(customerDAO).should(times(1)).get(10L);
        assertThat(returnedCustomer).isEqualTo(customer);
    }

    @Test
    public void getCustomer_shouldReturnNull_ifNotPresent() {
        given(customerDAO.get(anyLong())).willReturn(Optional.empty());

        Customer returnedCustomer = customerService.getCustomer(10L);

        then(customerDAO).should(times(1)).get(10L);
        assertThat(returnedCustomer).isNull();
    }

    @Test
    public void getCustomers_shouldReturnListOfCustomers_ifPresent() {
        Customer first = new Customer(10L, 4L, "first", "name");
        Customer second = new Customer(14L, 2L, "second", "name");
        given(customerDAO.getAll()).willReturn(Arrays.asList(first, second));

        List<Customer> customers = customerService.getCustomers();

        then(customerDAO).should(times(1)).getAll();
        assertThat(customers).contains(first);
        assertThat(customers).contains(second);
    }

    @Test
    public void getCustomers_shouldReturnNull_ifNoCustomersPresent() {
        given(customerDAO.getAll()).willReturn(null);

        List<Customer> customers = customerService.getCustomers();

        then(customerDAO).should(times(1)).getAll();
        assertThat(customers).isNullOrEmpty();
    }

    @Test
    public void createCustomer_callsDAOSaveMethod() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        doNothing().when(customerDAO).save(any());

        customerService.createCustomer(customer);
        then(customerDAO).should(times(1)).save(customer);
    }

    @Test(expected = CustomerCreationFailedException.class)
    public void createShift_shouldThrowCustomerCreationFailedException() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        doThrow(CustomerCreationFailedException.class).when(customerDAO).save(any());

        customerService.createCustomer(customer);
    }

    @Test
    public void updateCustomer_shouldVerifyThatCustomer_exists() {
        Customer customer = new Customer();
        customer.setId(10L);
        given(customerDAO.get(anyLong())).willReturn(Optional.of(customer));
        customerService.updateCustomer(customer);
        then(customerDAO).should(times(1)).get(10L);
    }

    @Test
    public void updateCustomer_shouldCallDAOUpdate_ifCustomerExists() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        given(customerDAO.get(anyLong())).willReturn(Optional.of(customer));
        doNothing().when(customerDAO).update(customer);

        customerService.updateCustomer(customer);

        then(customerDAO).should(times(1)).update(customer);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void updateCustomer_shouldThrowError_ifCustomerDoesNotExist() {
        Customer customer = new Customer();
        customer.setId(1L);
        given(customerDAO.get(anyLong())).willReturn(Optional.empty());

        customerService.updateCustomer(customer);
        then(customerDAO).should(times(1)).get(1L);
        then(customerDAO).should(times(0)).update(customer);
    }

    @Test
    public void getCustomerBookingsForDate_should_returnNull_ifNoBookingsInDatabase() {
        given(customerDAO.getBookingsForDate(anyLong(), any())).willReturn(null);

        Timestamp time = new Timestamp(new GregorianCalendar(2019, 7, 3).getTimeInMillis());
        List<Booking> bookings = customerService.getCustomerBookingsForDate(13L, time);
        then(customerDAO).should(times(1)).getBookingsForDate(13L, time);
        assertThat(bookings).isNullOrEmpty();
    }

    @Test
    public void getCustomerBookingsForDate_shouldReturnListOfBookings_forNonEmptyDatabase() {
        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);

        given(customerDAO.getBookingsForDate(anyLong(), any())).willReturn(Arrays.asList(first, second));

        List<Booking> bookings = customerService.getCustomerBookingsForDate(14L, START_1);

        then(customerDAO).should(times(1)).getBookingsForDate(14L, START_1);
        assertThat(bookings).contains(first);
        assertThat(bookings).contains(second);
    }

    @Test
    public void customerIsAvailable_returnsFalseForOverLappingBookings() {
        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);
        List<Booking> bookings = Arrays.asList(first, second);
        doReturn(bookings).when(spyService).getCustomerBookingsForDate(anyLong(), any());
        doReturn(true).when(bookingService).bookingTimeHasConflict(any(), any(), any());

        Boolean isAvailable = spyService.customerIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getCustomerBookingsForDate(12L, START_1);
        then(bookingService).should(times(1)).bookingTimeHasConflict(bookings, START_1, END_1);
        assertThat(isAvailable).isFalse();
    }

    @Test
    public void customerIsAvailable_returnsTrueForValidBookings() {
        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);
        List<Booking> bookings = Arrays.asList(first, second);
        doReturn(bookings).when(spyService).getCustomerBookingsForDate(anyLong(), any());
        doReturn(false).when(bookingService).bookingTimeHasConflict(any(), any(), any());

        Boolean isAvailable = spyService.customerIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getCustomerBookingsForDate(12L, START_1);
        then(bookingService).should(times(1)).bookingTimeHasConflict(bookings, START_1, END_1);
        assertThat(isAvailable).isTrue();
    }
}
