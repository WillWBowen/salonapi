//package com.salon.www.salonapi.controller;
//
//import com.salon.www.salonapi.model.Customer;
//import com.salon.www.salonapi.service.CustomerService;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//public class CustomerController {
//
//    private CustomerService customerService;
//
//    @Autowired
//    public CustomerController(CustomerService customerService) {
//        this.customerService = customerService;
//    }
//
//    @GetMapping("/customer")
//    @SneakyThrows
//    public ResponseEntity<List<Customer>> customers() {
//        HttpHeaders headers = new HttpHeaders();
//        List<Customer> customers = customerService.getCustomers();
//        if(customers == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        headers.add("Number of Records Found", String.valueOf(customers.size()));
//        return new ResponseEntity<>(customers, headers, HttpStatus.OK);
//    }
//
//    @GetMapping("/customer/{id}")
//    @SneakyThrows
//    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long customerId) {
//        Customer customer = customerService.getCustomer(customerId);
//        if(customer == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(customer, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/customer/{id}")
//    @SneakyThrows
//    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long customerId) {
//        HttpHeaders headers = new HttpHeaders();
//        Customer customer = customerService.getCustomer(customerId);
//        if(customer == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        customerService.deleteCustomer(customer);
//        headers.add("Customer Deleted - ", String.valueOf(customerId));
//        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
//    }
//
//    @PutMapping("/customer/{id}")
//    @SneakyThrows
//    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long customerId, @RequestBody Customer customer) {
//        HttpHeaders headers = new HttpHeaders();
//        Customer exists = customerService.getCustomer(customerId);
//        if (exists == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else if(customer == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        customerService.updateCustomer(customer);
//        headers.add("Customer Updated - ", String.valueOf(customerId));
//        return new ResponseEntity<>(headers, HttpStatus.OK);
//    }
//
//    @PostMapping("/customer")
//    @SneakyThrows
//    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
//        HttpHeaders headers = new HttpHeaders();
//        if(customer == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        customerService.createCustomer(customer);
//        headers.add("Customer Created - ", String.valueOf(customer.getId()));
//        return new ResponseEntity<>(headers, HttpStatus.OK);
//    }
//}
//
