package com.salon.www.salonapi.controller;

import com.google.gson.Gson;
import com.salon.www.salonapi.exception.BadInputException;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.itf.CustomerService;
import com.salon.www.salonapi.service.itf.EmployeeService;
import com.salon.www.salonapi.service.itf.ShiftService;
import com.salon.www.salonapi.service.itf.SkillService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private CustomerService customerService;
    private EmployeeService employeeService;
    private SkillService skillService;
    private ShiftService shiftService;

    private static final Gson gson = new Gson();

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() {
                List<Customer> customers = customerService.getCustomers();
        if(customers == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(customers));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable("id") Long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if(customer == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(customer));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        if(employees == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(employees));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") Long employeeId) {
       Employee employee = employeeService.getEmployee(employeeId);
       if(employee == null) {
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(employee));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        validateEmployee(employee);
        employeeService.createEmployee(employee);
        return ResponseEntity.created(null).contentType(MediaType.APPLICATION_JSON_UTF8).build();
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/employees")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee) {
        validateEmployee(employee);
        if(employee.getId() == null) return ResponseEntity.badRequest().body("Employee Id is required for update");
        employeeService.updateEmployee(employee);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/skills")
    public ResponseEntity<?> createSkill(@RequestBody Skill skill) {
        validateSkill(skill);
        skillService.createSkill(skill);
        return ResponseEntity.created(null).contentType(MediaType.APPLICATION_JSON_UTF8).build();
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/skills")
    public ResponseEntity<?> updateSkill(@RequestBody Skill skill) {
        validateSkill(skill);
        if(skill.getId() == 0) return ResponseEntity.badRequest().body("Skill Id required for update.");
        skillService.updateSkill(skill);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/skills/{id}")
    public ResponseEntity<?> getSkill(@PathVariable("id") Long id) {
        Skill skill = skillService.getSkill(id);
        if(skill == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(skill));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/skills")
    public ResponseEntity<?> getSkills() {
        List<Skill> skills = skillService.getSkills();
        if (skills == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(skills));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/shifts")
    public ResponseEntity<?> createShift(@RequestBody EmployeeShift shift) {
        log.info("validating shift: " + shift);
        validateShift(shift);
        log.info("shift was validated");
        shiftService.createShift(shift);
        return ResponseEntity.created(null).contentType(MediaType.APPLICATION_JSON_UTF8).build();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/shifts/{id}")
    public ResponseEntity<?> getShift(@PathVariable("id") Long shiftId) {
        log.info("Shift Id: " + shiftId);
        EmployeeShift shift = shiftService.getShift(shiftId);
        if(shift == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(shift));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/shifts")
    public ResponseEntity<?> getShifts() {
        List<EmployeeShift> shifts = shiftService.getShifts();
        if(shifts == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(shifts));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/shifts")
    public ResponseEntity<?> updateShift(@RequestBody EmployeeShift shift) {
        validateShift(shift);
        if(shift.getId() == null) return ResponseEntity.badRequest().body(gson.toJson("Shift Id is required for update."));
        shiftService.updateShift(shift);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({BadInputException.class})
    public ResponseEntity<?> handleAuthenticationException(BadInputException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(e.getMessage()));
    }

    private void validateEmployee(Employee employee) throws BadInputException {
        String errorMessage = "";
        if(employee.getUserId() == null) {
            errorMessage += "UserId cannot be null.\n";
        }
        if(employee.getFirstName() == null) {
            errorMessage += "firstName cannot be null.\n";
        }
        if(employee.getLastName() == null) {
            errorMessage += "lastName cannot be null.\n";
        }
        if(errorMessage.length() > 0) throw new BadInputException(errorMessage);
    }

    private void validateSkill(Skill skill) throws BadInputException {
        String errorMessage = "";
        if(skill.getPrice() == 0) {
            errorMessage += "Price cannot be zero.\n";
        }
        if(skill.getName() == null) {
            errorMessage += "Name cannot be null.\n";
        }
        if(errorMessage.length() > 0) throw new BadInputException(errorMessage);
    }

    private void validateShift(EmployeeShift shift) throws BadInputException {
        String errorMessage = "";
        if(employeeService.getEmployee(shift.getEmployeeId()) == null) {
            errorMessage += "Employee does not exist.\n";
        }
        if(shift.getEndTime().isBefore(shift.getStartTime())) {
            errorMessage += "Shift cannot end before it starts.\n";
        }
        if(errorMessage.length() > 0) {
            throw new BadInputException(errorMessage);
        }
    }
}
