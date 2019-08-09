package com.salon.www.salonapi.controller;

import com.google.gson.Gson;
import com.salon.www.salonapi.exception.BadInputException;
import com.salon.www.salonapi.exception.CustomerCreationFailedException;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.model.NewUser;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.service.itf.UserService;
import com.salon.www.salonapi.service.itf.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.SneakyThrows;

@Log4j2
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Gson gson = new Gson();

    private CustomerService customerService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerController(CustomerService customerService, UserService userService, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping("/new")
    @SneakyThrows
    public ResponseEntity<Customer> createCustomer(@RequestBody NewUser newUser) {
        log.info("Request creation of new user: " + newUser);
        validateNewUser(newUser);
        createNewUser(newUser);
        User user = userService.getUser(newUser.getUsername());
        Customer newCustomer = new Customer(newUser);
        newCustomer.setUserId(user.getId());
        customerService.createCustomer(newCustomer);
        Customer customer = customerService.getCustomerByEmail(newCustomer.getEmail());
        log.info("Created customer: " + newCustomer);
        return ResponseEntity.created(null).contentType(MediaType.APPLICATION_JSON).body(customer);
    }

    @ExceptionHandler({BadInputException.class})
    public ResponseEntity<String> handleAuthenticationException(BadInputException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({CustomerCreationFailedException.class})
    public ResponseEntity<String> handleCustomerCreationFailedException(CustomerCreationFailedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(e.getMessage()));
    }

    private void createNewUser(NewUser newUser) {
        if(userService.getUser(newUser.getUsername()) != null) {
            throw new CustomerCreationFailedException("Username already exists");
        }
        if(customerService.getCustomerByEmail(newUser.getEmail()) != null) {
            throw new CustomerCreationFailedException("Customer already exists");
        }
        User user = new User(newUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userService.createUser(user);
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

    private void validateNewUser(NewUser newUser) throws BadInputException {
        String errorMessage = "";
        if(newUser.getUsername() == null) {
            errorMessage += "Username cannot be null.\n";
        }
        if(newUser.getPassword() == null) {
            errorMessage += "Password cannot be null.\n";
        }
        if(newUser.getPhone() == null) {
            errorMessage += "Phone cannot be null.\n";
        }
        if(newUser.getEmail() == null) {
            errorMessage += "Email cannot be null.\n";
        }
        if(newUser.getFirstName() == null) {
            errorMessage += "firstName cannot be null.\n";
        }
        if(newUser.getLastName() == null) {
            errorMessage += "lastName cannot be null.\n";
        }
        if(errorMessage.length() > 0) throw new BadInputException(errorMessage);
    }
}
