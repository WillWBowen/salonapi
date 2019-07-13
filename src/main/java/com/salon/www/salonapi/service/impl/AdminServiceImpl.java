package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.EmployeeDAO;
import com.salon.www.salonapi.dao.EmployeeShiftDAO;
import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

    private EmployeeDAO employeeDao;
    private EmployeeShiftDAO employeeShiftDao;
    private SkillDAO skillDao;

    @Autowired
    public AdminServiceImpl(EmployeeDAO employeeDao, EmployeeShiftDAO employeeShiftDao, SkillDAO skillDao) {
        this.employeeDao = employeeDao;
        this.employeeShiftDao = employeeShiftDao;
        this.skillDao = skillDao;
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeDao.update(employee);
    }

    @Override
    public void addEmployeeSkill(Employee employee, Skill skill) {
        employeeDao.addSkill(employee, skill);
    }

    @Override
    public void addEmployeeSkills(Employee employee, List<Skill> skills) {
        for(Skill skill : skills) {
            employeeDao.addSkill(employee, skill);
        }
    }

    @Override
    public void removeEmployeeSkill(Employee employee, Skill skill) {
        employeeDao.removeSkill(employee, skill);
    }

    @Override
    public List<Skill> getEmployeeSkills(Employee employee) {
        return skillDao.getForEmployee(employee);
    }

    @Override
    public void addEmployeeShift(EmployeeShift shift) {
        employeeShiftDao.save(shift);
    }

    @Override
    public void removeEmployeeShift(EmployeeShift shift) {
        employeeShiftDao.delete(shift);
    }

    @Override
    public List<EmployeeShift> getEmployeeShifts(Employee employee) {
        return employeeShiftDao.getAllForEmployee(employee);
    }

    @Override
    public EmployeeShift getEmployeeShiftForDay(Employee employee, String day) {
        return employeeShiftDao.getForEmployeeForDay(employee, day).orElse(null);
    }

    @Override
    public void createSkill(Skill skill) {
        skillDao.save(skill);
    }

    @Override
    public void updateSkill(Skill skill) {
        skillDao.update(skill);
    }
}
