package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.service.itf.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("customerService")
@Transactional
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
}
