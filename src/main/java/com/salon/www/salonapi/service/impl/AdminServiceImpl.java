package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.EmployeeDAO;
import com.salon.www.salonapi.dao.EmployeeShiftDAO;
import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminServiceImpl implements AdminService {

    @Autowired
    private EmployeeDAO employeeDAO;
    @Autowired
    private EmployeeShiftDAO shiftDAO;
    @Autowired
    private SkillDAO skillDAO;

    @Override
    public void addEmployee(Employee employee) {
        employeeDAO.save(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeDAO.update(employee);
    }

    @Override
    public void addEmployeeSkill(Employee employee, Skill skill) {
        employeeDAO.addSkill(employee, skill);
    }

    @Override
    public void addEmployeeSkills(Employee employee, List<Skill> skills) {
        for(Skill skill : skills) {
            employeeDAO.addSkill(employee, skill);
        }
    }

    @Override
    public void removeEmployeeSkill(Employee employee, Skill skill) {
        employeeDAO.removeSkill(employee, skill);
    }

    @Override
    public List<Skill> getEmployeeSkills(Employee employee) {
        return skillDAO.getForEmployee(employee);
    }

    @Override
    public void addEmployeeShift(EmployeeShift shift) {
        shiftDAO.save(shift);
    }

    @Override
    public void removeEmployeeShift(EmployeeShift shift) {
        shiftDAO.delete(shift);
    }

    @Override
    public List<EmployeeShift> getEmployeeShifts(Employee employee) {
        return shiftDAO.getAllForEmployee(employee);
    }

    @Override
    public EmployeeShift getEmployeeShiftForDay(Employee employee, String day) {
        return shiftDAO.getForEmployeeForDay(employee, day).get();
    }

    @Override
    public void createSkill(Skill skill) {
        skillDAO.save(skill);
    }

    @Override
    public void updateSkill(Skill skill) {
        skillDAO.update(skill);
    }
}
