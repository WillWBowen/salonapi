package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.exception.CustomerNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Customer;

import java.sql.Timestamp;
import java.util.List;

public interface CustomerService {
    List<Customer> getCustomers();
    Customer getCustomer(Long customerId);

    List<Booking> getCustomerBookingsForDate(Long customerId, Timestamp date);

    Boolean customerIsAvailable(long customerId, Timestamp bookingTime, Timestamp endTime);

    void updateCustomer(Customer customer);

    void createCustomer(Customer customer);
}
