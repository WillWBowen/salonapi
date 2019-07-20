package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")

public class AdminController {

    private CustomerService customerService;

    @Autowired
    public AdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomersAsAdmin() {
        HttpHeaders headers = new HttpHeaders();
        List<Customer> customers = customerService.getCustomers();
        if(customers == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number of Records Found", String.valueOf(customers.size()));
        return new ResponseEntity<>(customers, headers, HttpStatus.OK);
    }

}
