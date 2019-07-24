package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Customer;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO extends Dao<Customer> {

    Optional<Customer> getCustomerByEmail(String email);

    List<Booking> getBookingsForDate(Long customerId, Timestamp date);
}
