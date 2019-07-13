package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.dao.CustomerDAO;
import com.salon.www.salonapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerDao.getAll();
    }

    public Customer getCustomer(Long customerId) {
        return customerDao.get(customerId).orElse(null);
    }

    public Customer getCustomerByEmail(String email) {
        return customerDao.getCustomerByEmail(email).orElse(null);
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
