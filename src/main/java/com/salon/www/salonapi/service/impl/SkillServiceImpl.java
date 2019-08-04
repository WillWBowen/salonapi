package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.itf.SkillService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service("skillService")
@Transactional
public class SkillServiceImpl implements SkillService {

    private SkillDAO skillDao;

    @Autowired
    public SkillServiceImpl(SkillDAO skillDao) {
        this.skillDao = skillDao;
    }

    @Override
    public Skill getSkill(Long id) {
        return skillDao.get(id).orElseThrow(SkillNotFoundException::new);
    }

    @Override
    public List<Skill> getSkills() {
        return skillDao.getAll();
    }

    @Override
    @Transactional
    public void updateSkill(Skill skill) {
        skillDao.update(skill);
    }

    @Override
    public void createSkill(Skill skill) {
       log.info("Save the skill {}", skill);
       skillDao.save(skill);

    }
}
