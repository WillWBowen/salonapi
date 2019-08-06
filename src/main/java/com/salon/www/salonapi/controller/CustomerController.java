package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.exception.BadInputException;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.service.itf.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.SneakyThrows;

@Log4j2
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        if(customers == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if(customer == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping
    @SneakyThrows
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        validateCustomer(customer);
        if(customer.getId() == null) return ResponseEntity.badRequest().body("Customer Id is required for update.");
        customerService.updateCustomer(customer);
        return ResponseEntity.noContent().build();

    }

    @PostMapping
    @SneakyThrows
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        HttpHeaders headers = new HttpHeaders();
        validateCustomer(customer);
        customerService.createCustomer(customer);
        headers.add("Customer Created - ", String.valueOf(customer.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @ExceptionHandler({BadInputException.class})
    public ResponseEntity<String> handleAuthenticationException(BadInputException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private void validateCustomer(Customer customer) throws BadInputException {
        String errorMessage = "";
        if(customer.getUserId() == null) {
            errorMessage += "UserId cannot be null.\n";
        }
        if(customer.getFirstName() == null) {
            errorMessage += "firstName cannot be null.\n";
        }
        if(customer.getLastName() == null) {
            errorMessage += "lastName cannot be null.\n";
        }
        if(errorMessage.length() > 0) throw new BadInputException(errorMessage);
    }
}
