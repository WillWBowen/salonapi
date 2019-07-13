package com.salon.www.salonapi.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(long customer_id) {
        super(String.format("Customer is not found with id : '%s'", customer_id));
    }
}
