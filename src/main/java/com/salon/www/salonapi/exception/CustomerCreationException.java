package com.salon.www.salonapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Customer creation was not successful") //500
public class CustomerCreationException extends CreationException{

    public CustomerCreationException() {
        super("Customer creation was unsuccessful");
    }
}
