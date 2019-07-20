package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.exception.SkillCreationFailedException;
import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.SkillService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Log4j2
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
       log.info("Save the skill {}", skill);
       skillDao.save(skill);

    }
}
