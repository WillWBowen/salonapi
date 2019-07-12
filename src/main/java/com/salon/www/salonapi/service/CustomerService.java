package com.salon.www.salonapi.service;

import com.salon.www.salonapi.model.Customer;

import java.util.List;

public interface CustomerService {
    public List<Customer> getCustomers();
    public Customer getCustomer(Long customerId);
    public Customer getCustomerByEmail(String customerId);
    public void deleteCustomer(Customer customer);
    public void updateCustomer(Customer customer);
    public void createCustomer(Customer customer);
}
