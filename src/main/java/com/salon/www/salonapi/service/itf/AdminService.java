package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;

import java.util.List;

public interface AdminService {
    void addEmployeeSkill(Employee employee, Skill skill);
    void addEmployeeSkills(Employee employee, List<Skill> skills);
    void removeEmployeeSkill(Employee employee, Skill skill);
    List<Skill> getEmployeeSkills(Employee employee);
    void addEmployeeShift(EmployeeShift shift);
    void removeEmployeeShift(EmployeeShift shift);
    List<EmployeeShift> getEmployeeShifts(Employee employee);
    EmployeeShift getEmployeeShiftForDay(Employee employee, String day);
    void createSkill(Skill skill);
    void updateSkill(Skill skill);
}
