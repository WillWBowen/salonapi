package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.service.CustomerService;
import com.salon.www.salonapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")

public class AdminController {

    private CustomerService customerService;
    private EmployeeService employeeService;

    @Autowired
    public AdminController(CustomerService customerService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomersAsAdmin() {
                List<Customer> customers = customerService.getCustomers();
        if(customers == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerAsAdmin(@PathVariable("id") Long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if(customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployeesAsAdmin() {
        List<Employee> employees = employeeService.getEmployees();
        if(employees == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeAsAdmin(@PathVariable("id") Long employeeId) {
       Employee employee = employeeService.getEmployee(employeeId);
       if(employee == null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        String errorMessage = "";
        if(employee.getUserId() == null) {
            errorMessage += "UserId cannot be blank.\n";
        }
        if(employee.getFirstName() == null) {
            errorMessage += "firstName cannot be blank.\n";
        }
        if(employee.getLastName() == null) {
            errorMessage += "lastName cannot be blank.\n";
        }
        if(errorMessage.length() > 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        employeeService.createEmployee(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
