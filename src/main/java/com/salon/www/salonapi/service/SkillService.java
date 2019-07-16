package com.salon.www.salonapi.service;

import com.salon.www.salonapi.model.Skill;

public interface SkillService {
    Skill getSkill(Long id);
    void createSkill(Skill skill);
}
