package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.impl.SkillServiceImpl;
import com.salon.www.salonapi.service.itf.SkillService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        given(skillDao.get(anyLong())).willReturn(Optional.of(new Skill(1L, "manicure", 20)));

        Skill skill = skillService.getSkill(1L);

        then(skillDao).should(times(1)).get(1L);
        assertThat(skill.getName()).isEqualTo("manicure");
        assertThat(skill.getPrice()).isEqualTo(20);
        assertThat(skill.getId()).isEqualTo(1L);
    }

    @Test(expected = SkillNotFoundException.class)
    public void getSkill_whenSkillNotFound() {
        given(skillDao.get(anyLong())).willReturn(Optional.empty());

        skillService.getSkill(1L);
        then(skillDao).should(times(1)).get(1L);
    }

    @Test
    public void getSkills_returnsListOfSkills() {
        Skill first = new Skill(1L, "manicure", 20);
        Skill second = new Skill(2L, "pedicure", 30);
        given(skillDao.getAll()).willReturn(Arrays.asList(first, second));

        List<Skill> skills = skillService.getSkills();

        then(skillDao).should(times(1)).getAll();
        assertThat(skills.size()).isEqualTo(2);
        assertThat((skills.contains(first))).isTrue();
        assertThat((skills.contains(second))).isTrue();
    }

    @Test
    public void getSkills_returnsNull_forEmptyDatabase() {
        given(skillDao.getAll()).willReturn(null);

        List<Skill> skills = skillService.getSkills();

        then(skillDao).should(times(1)).getAll();
        assertThat(skills).isNullOrEmpty();
    }

    @Test
    public void postSkill_ShouldCallSkillDaoWithCorrectObject() {
        Skill skill = new Skill("manicure", 20);
        doNothing().when(skillDao).save(any());

        skillService.createSkill(skill);
        then(skillDao).should(times(1)).save(skill);
    }

    @Test
    public void updateSkill_ShouldCallDAOupdateMethodWithObject() {
        Skill skill = new Skill(1L, "manicure", 20);
        doNothing().when(skillDao).update(skill);

        skillService.updateSkill(skill);
        then(skillDao).should(times(1)).update(skill);
    }
}
