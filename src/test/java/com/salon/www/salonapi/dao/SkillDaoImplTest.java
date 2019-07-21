package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.SkillCreationFailedException;
import com.salon.www.salonapi.exception.SkillDeletionFailedException;
import com.salon.www.salonapi.exception.SkillUpdateFailedException;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillDaoImplTest {

    private static final String CREATE_SKILL_T_SQL_SCRIPT = "scripts/create/skills_t.sql";
    private static final String DROP_SKILL_T_SQL_SCRIPT = "scripts/drop/skills_t.sql";
    private static final String POPULATE_ONE_SKILL_T_SQL_SCRIPT = "scripts/populate/one_skill_t.sql";
    private static final String POPULATE_TWO_SKILLS_T_SQL_SCRIPT = "scripts/populate/two_skills_t.sql";
    private static final String POPULATE_EMPLOYEE_SKILL_T_SQL_SCRIPT = "scripts/populate/employee_skills_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SkillDAO skillDao;


    @Before
    public void setUp() throws ScriptException, SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_SKILL_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws ScriptException, SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_SKILL_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddSkillToDatabase() {
        Skill skill = new Skill("manicure", 20);
        skillDao.save(skill);

        Optional<Skill> validSkill = skillDao.get(1L);

        assertThat(validSkill.isPresent()).isEqualTo(true);
        assertThat(validSkill.get().getName()).isEqualTo(skill.getName());
        assertThat(validSkill.get().getPrice()).isEqualTo(skill.getPrice());
    }

    @Test(expected = SkillCreationFailedException.class)
    public void save_shouldThrowError_forInvalidSkillObject() {
        Skill skill = new Skill();
        skill.setName("This string is going to be too long to fit into the database");

        skillDao.save(skill);
    }
    @Test
    public void get_shouldReturnValidSkill_forExistingSkill() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_SKILL_T_SQL_SCRIPT));
        connection.close();
        Optional<Skill> validSkill = skillDao.get(1L);

        assertThat(validSkill.isPresent()).isEqualTo(true);
        assertThat(validSkill.get().getName()).isEqualTo("manicure");
        assertThat(validSkill.get().getPrice()).isEqualTo(20);
    }

    @Test
    public void get_shouldReturnInvalidSkill_forEmptyDatabase() {
        Optional<Skill> invalid = skillDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Skill> noSkills = skillDao.getAll();

        assertThat(noSkills).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfSkills_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_SKILLS_T_SQL_SCRIPT));
        connection.close();

        List<Skill> skills = skillDao.getAll();

        assertThat(skills).isNotNull().hasSize(2);
        assertThat(skills.contains(new Skill(1L, "manicure", 20))).isTrue();
        assertThat(skills.contains(new Skill(2L, "pedicure", 30))).isTrue();

    }

    @Test(expected = SkillUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingSkill() {
        Skill notFound = new Skill();
        notFound.setId(new Random().nextLong());

        skillDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingSkill() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_SKILL_T_SQL_SCRIPT));
        connection.close();

        skillDao.update(new Skill(1L,"pedicure", 30));

        Optional<Skill> updatedSkill = skillDao.get(1L);
        assertThat(updatedSkill).isPresent();
        assertThat(updatedSkill.get().getName()).isEqualTo("pedicure");
        assertThat(updatedSkill.get().getPrice()).isEqualTo(30);
    }

    @Test
    public void delete_shouldRemoveSkillFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_SKILL_T_SQL_SCRIPT));
        connection.close();

        skillDao.delete(new Skill(1L, "", 0));

        assertThat(skillDao.get(1L).isPresent()).isFalse();
        assertThat(skillDao.getAll()).hasSize(0);
    }

    @Test(expected = SkillDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Skill skill = new Skill();
        skill.setId(new Random().nextLong());

        skillDao.delete(skill);
    }

    @Test
    public void getForEmployee_shouldYieldListOfSkills_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_EMPLOYEE_SKILL_T_SQL_SCRIPT));
        connection.close();

        List<Skill> skills = skillDao.getForEmployee(1L);
        assertThat(skills).isNotNull().hasSize(1);

        Skill result = skills.get(0);
        assertThat(result).hasFieldOrPropertyWithValue("name", "pedicure");
        assertThat(result).hasFieldOrPropertyWithValue("price", 30);
    }

}
