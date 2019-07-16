package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.exception.SkillCreationFailedException;
import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.SkillService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service("skillService")
public class SkillServiceImpl implements SkillService {

    private SkillDAO skillDao;

    public SkillServiceImpl(SkillDAO skillDao) {
        this.skillDao = skillDao;
    }

    @Override
    public Skill getSkill(Long id) {
        return skillDao.get(id).orElseThrow(SkillNotFoundException::new);
    }

    @Override
    public void createSkill(Skill skill) {
            skillDao.save(skill);

    }
}
