package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.service.itf.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.SneakyThrows;

@RestController
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        if(customers == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if(customer == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/customers/{id}")
    @SneakyThrows
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long customerId, @RequestBody Customer customer) {
        HttpHeaders headers = new HttpHeaders();
        Customer exists = customerService.getCustomer(customerId);
        if (exists == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if(customer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        customerService.updateCustomer(customer);
        headers.add("Customer Updated - ", String.valueOf(customerId));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/customers")
    @SneakyThrows
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        HttpHeaders headers = new HttpHeaders();
        if(customer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        customerService.createCustomer(customer);
        headers.add("Customer Created - ", String.valueOf(customer.getId()));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
