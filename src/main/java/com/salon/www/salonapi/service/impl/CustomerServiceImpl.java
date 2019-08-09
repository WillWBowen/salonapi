package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.exception.CustomerNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.service.itf.BookingService;
import com.salon.www.salonapi.service.itf.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDao;
    private BookingService bookingService;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDao, BookingService bookingService) {
        this.customerDao = customerDao;
        this.bookingService = bookingService;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerDao.getAll();
    }

    public Customer getCustomer(Long customerId) {
        return customerDao.get(customerId).orElse(null);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerDao.getCustomerByEmail(email).orElse(null);
    }

    public List<Booking> getCustomerBookingsForDate(Long customerId, Timestamp date) {
        return customerDao.getBookingsForDate(customerId, date);
    }

    @Override
    public Boolean customerIsAvailable(long customerId, Timestamp bookingTime, Timestamp endTime) {
        // Check no other bookings overlap this booking
        List<Booking> bookings = getCustomerBookingsForDate(customerId, bookingTime);
        return !bookingService.bookingTimeHasConflict(bookings, bookingTime, endTime);
    }

    @Override
    public void updateCustomer(Customer customer) {
        //verify object exists
        Optional<Customer> oldCustomer = customerDao.get(customer.getId());
        oldCustomer.orElseThrow(CustomerNotFoundException::new);
        customerDao.update(customer);
    }

    @Override
    public void createCustomer(Customer customer) {
        customerDao.save(customer);
    }
}
