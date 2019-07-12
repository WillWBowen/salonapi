package com.salon.www.salonapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class CreationException extends Exception {

    public CreationException(String s){
        super(s);
    }
}
