package com.salon.www.salonapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.model.Skill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.script.ScriptException;
import java.sql.SQLException;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SkillsIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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
    @WithMockUser(roles = "USER")
    public void getSkill_returnsSkillDetails() throws Exception{

        //arrange
        skillDao.save(new Skill(1L, "manicure", 20));
        //act
        mvc.perform(MockMvcRequestBuilders.get("/skills/1"))
                //assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name").value("manicure"))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void postSkill_addsNewSkillToDatabase() throws Exception {
        //arrange
        Skill skill = new Skill("pedicure", 30);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(skill);

        //act
        mvc.perform(MockMvcRequestBuilders.post("/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                // assert
                .andExpect(status().isCreated());
    }
}
