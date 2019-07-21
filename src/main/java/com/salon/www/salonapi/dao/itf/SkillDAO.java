package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.dao.itf.Dao;
import com.salon.www.salonapi.model.Skill;

import java.util.List;

public interface SkillDAO extends Dao<Skill> {
    List<Skill> getForEmployee(Long employee_id);
}
