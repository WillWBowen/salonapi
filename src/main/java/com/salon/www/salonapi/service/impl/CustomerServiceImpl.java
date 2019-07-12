package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.RoleDAO;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.dao.CustomerDAO;
import com.salon.www.salonapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDAO customerDao;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = customerDao.getAll();
        return customers;
    }

    public Customer getCustomer(Long customerId) {
        Customer customer = customerDao.get(customerId).get();
        return customer;
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = customerDao.getCustomerByEmail(email).get();
        return customer;
    }

    public void deleteCustomer(Customer customer) {
        customerDao.delete(customer);
    }

    public void updateCustomer(Customer customer) {
        customerDao.update(customer);
    }

    public void createCustomer(Customer customer) {
        customerDao.save(customer);
    }
}
