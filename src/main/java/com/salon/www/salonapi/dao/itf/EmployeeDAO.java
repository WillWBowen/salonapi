package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.dao.itf.Dao;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EmployeeDAO extends Dao<Employee> {
    void addSkill(Employee employee, Skill skill);
    void removeSkill(Employee employee, Skill skill);

    Optional<EmployeeShift> getShiftForDay(Long employeeId, int day);

    List<Booking> getBookingsForDate(Long employeeId, Timestamp date);
}
