package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.impl.SkillServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SkillServiceTest {

    @Mock
    private SkillDAO skillDao;

    private SkillService skillService;

    @Before
    public void setUp() {
        skillService = new SkillServiceImpl(skillDao);
    }

    @Test
    public void getSkill_returnsSkillInfo() {
        given(skillDao.get(1L)).willReturn(Optional.of(new Skill(1L, "manicure", 20)));

        Skill skill = skillService.getSkill(1L);

        assertThat(skill.getName()).isEqualTo("manicure");
        assertThat(skill.getPrice()).isEqualTo(20);
        assertThat(skill.getId()).isEqualTo(1L);
    }

    @Test(expected = SkillNotFoundException.class)
    public void getSkill_whenSkillNotFound() {
        given(skillDao.get(1L)).willReturn(Optional.empty());

        skillService.getSkill(1L);
    }
}
