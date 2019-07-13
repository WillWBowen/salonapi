package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.model.Customer;

import java.util.Optional;

public interface CustomerDAO extends Dao<Customer>{

    Optional<Customer> getCustomerByEmail(String email);

}
