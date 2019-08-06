package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.dao.itf.Dao;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;

import java.util.List;
import java.util.Optional;

public interface EmployeeShiftDAO extends Dao<EmployeeShift> {
    List<EmployeeShift> getAllForEmployee(Employee employee);
    Optional<EmployeeShift> getForEmployeeForDay(Employee employee, int day);
}
