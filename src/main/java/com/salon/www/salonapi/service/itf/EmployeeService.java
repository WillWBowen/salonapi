package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;

import java.sql.Timestamp;
import java.util.List;

public interface EmployeeService {
    List<Employee> getEmployees();
    Employee getEmployee(Long id);
    void createEmployee(Employee employee);
    void updateEmployee(Employee employee);

    List<Skill> getEmployeeSkills(long employeeId);

    EmployeeShift getEmployeeShiftForDay(Long employeeId, int day);

    List<Booking> getEmployeeBookingsForDate(Long employeeId, Timestamp date);

    Boolean employeeIsAvailable(long employeeId, Timestamp bookingTime, Timestamp endTime);

    Boolean employeeHasSkills(long employeeId, List<Skill> skills);
}
