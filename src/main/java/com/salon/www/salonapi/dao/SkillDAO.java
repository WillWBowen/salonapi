package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.Skill;

import java.util.List;

public interface SkillDAO extends Dao<Skill> {
    List<Skill> getForEmployee(Long employee_id);
}
