package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getEmployees();
    Employee getEmployee(Long id);
    void createEmployee(Employee employee);
    void updateEmployee(Employee employee);
}
