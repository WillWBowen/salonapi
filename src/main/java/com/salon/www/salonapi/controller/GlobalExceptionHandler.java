package com.salon.www.salonapi.controller;


import com.salon.www.salonapi.exception.CustomerCreationException;
import com.salon.www.salonapi.exception.CustomerNotFoundException;
import com.salon.www.salonapi.model.ResponseErrorEntity;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Customer Creation unsuccessful")
    @ExceptionHandler(RuntimeException.class)
    public void handleCustomerCreationException(RuntimeException e) {
        log.error("The creation Error", e);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleAppointmentCreationException(Exception e, HttpServletRequest request) {

        log.error("error: {} ", e.getMessage() , e );
        return new ResponseEntity<>("Error from server. Method " + request.getMethod() + " to " + request.getRequestURI() +" could not be found. Please contact to PS for more detail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ResponseStatus(value= HttpStatus.METHOD_NOT_ALLOWED, reason="Method Not Allowed.")
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handle405Status() {
       return new ResponseEntity<>("Method Not Allowed.", HttpStatus.METHOD_NOT_ALLOWED);
    }

   // @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Endpoint not found")
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handle404Status(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Page Not Found.", HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabledUser() {
        return new ResponseEntity<>("User is disabled", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials() {
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.FORBIDDEN);
    }
}
