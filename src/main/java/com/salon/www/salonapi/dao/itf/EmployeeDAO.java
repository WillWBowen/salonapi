package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.dao.itf.Dao;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.Skill;

public interface EmployeeDAO extends Dao<Employee> {
    void addSkill(Employee employee, Skill skill);
    void removeSkill(Employee employee, Skill skill);
}
