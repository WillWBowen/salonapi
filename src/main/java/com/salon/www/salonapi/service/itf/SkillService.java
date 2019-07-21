package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.Skill;

import java.util.List;

public interface SkillService {
    Skill getSkill(Long id);
    List<Skill> getSkills();
    void createSkill(Skill skill);
    void updateSkill(Skill skill);
}
