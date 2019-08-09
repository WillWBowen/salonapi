package com.salon.www.salonapi.exception;

public class CustomerCreationFailedException extends RuntimeException {
    public CustomerCreationFailedException(String error) {
        super(error);
    }
}
