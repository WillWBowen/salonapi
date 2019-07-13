package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.model.Skill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptException;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillDaoImplTest {

    private static final Long SKILL_1_ID = 1L;
    private static final String SKILL_1_NAME = "Manicure";
    private static final int SKILL_1_PRICE = 20;

    private static final Long SKILL_2_ID = 2L;
    private static final String SKILL_2_Name = "Pedicure";
    private static final int SKILL_2_PRICE = 30;

    private static final String CREATE_SKILL_T_SQL_SCRIPT = "scripts/create/skills_t.sql";
    private static final String DROP_SKILL_T_SQL_SCRIPT = "scripts/drop/skills_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SkillDAO skillDao;


    @Before
    public void setUp() throws ScriptException, SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(CREATE_SKILL_T_SQL_SCRIPT));
    }

    @After
    public void tearDown() throws ScriptException, SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(DROP_SKILL_T_SQL_SCRIPT));
    }

    @Test
    public void get_shouldReturnValidCustomer_forExistingCustomer() {
        Skill skill_1 = new Skill(SKILL_1_ID, SKILL_1_NAME, SKILL_1_PRICE);
        skillDao.save(skill_1);
        Optional<Skill> validSkill = skillDao.get(skill_1.getId());

        assertThat(validSkill.isPresent()).isEqualTo(true);
        assertThat(validSkill.get().getName()).isEqualTo(skill_1.getName());
        assertThat(validSkill.get().getPrice()).isEqualTo(skill_1.getPrice());
    }

    @Test
    public void get_shouldReturnInvalidCustomer_forEmptyDatabase() {
        Optional<Skill> invalid = skillDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }
}
