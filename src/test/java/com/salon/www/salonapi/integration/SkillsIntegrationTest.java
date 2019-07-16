package com.salon.www.salonapi.integration;

import com.salon.www.salonapi.dao.SkillDAO;
import com.salon.www.salonapi.model.Skill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptException;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SkillsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SkillDAO skillDao;

    private static final String CREATE_SKILL_T_SQL_SCRIPT = "scripts/create/skills_t.sql";
    private static final String DROP_SKILL_T_SQL_SCRIPT = "scripts/drop/skills_t.sql";

    @Before
    public void setUp() throws ScriptException, SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(CREATE_SKILL_T_SQL_SCRIPT));
    }

    @After
    public void tearDown() throws ScriptException, SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(DROP_SKILL_T_SQL_SCRIPT));
    }

    @Test
    public void getSkill_returnsSkillDetails() {

        //arrange
        skillDao.save(new Skill(1L, "manicure", 20));
        //act
        ResponseEntity<Skill> response = restTemplate.getForEntity("/skills/1", Skill.class);
        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("manicure");
        assertThat(response.getBody().getPrice()).isEqualTo(20);
    }

    @Test
    public void postSkill_addsNewSkillToDatabase() throws Exception {
        //arrange
        Skill skill = new Skill("pedicure", 30);
        Skill postedSkill;
        //act
        ResponseEntity<Skill> response = restTemplate.postForEntity("/skills", skill, Skill.class);
        postedSkill = skillDao.get(1L).get();
        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postedSkill.getName()).isEqualTo("pedicure");
        assertThat(postedSkill.getPrice()).isEqualTo(30);


    }
}
