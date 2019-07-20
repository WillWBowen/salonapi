package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    public List<Employee> getEmployees() {
        return null;
    }

    public Employee getEmployee(Long id) {
        return null;
    }

    public void createEmployee(Employee employee) {
        // Blank for now
    }
}
