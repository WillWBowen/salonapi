package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomers();
    Customer getCustomer(Long customerId);
}