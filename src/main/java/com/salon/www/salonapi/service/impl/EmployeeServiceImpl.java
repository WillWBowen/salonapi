package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.EmployeeDAO;
import com.salon.www.salonapi.exception.EmployeeNotFoundException;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.service.itf.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDAO;

    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getEmployees() {
        return employeeDAO.getAll();
    }

    public Employee getEmployee(Long id) {
        return employeeDAO.get(id).orElse(null);
    }

    public void createEmployee(Employee employee) {
        employeeDAO.save(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeDAO.get(employee.getId()).orElseThrow(EmployeeNotFoundException::new);
        employeeDAO.update(employee);
    }
}
