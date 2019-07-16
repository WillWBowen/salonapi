package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.exception.AdministratorCreationFailedException;
import com.salon.www.salonapi.exception.AdministratorDeletionFailedException;
import com.salon.www.salonapi.exception.AdministratorUpdateFailedException;
import com.salon.www.salonapi.model.Administrator;
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


import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdministratorDaoImplTest {

    private static final String CREATE_ADMINISTRATOR_T_SQL_SCRIPT = "scripts/create/administrators_t.sql";
    private static final String DROP_ADMINISTRATOR_T_SQL_SCRIPT = "scripts/drop/administrators_t.sql";
    private static final String POPULATE_ONE_USER_T_SQL_SCRIPT = "scripts/populate/one_user_t.sql";
    private static final String POPULATE_ONE_ADMINISTRATOR_T_SQL_SCRIPT = "scripts/populate/one_administrator_t.sql";
    private static final String POPULATE_TWO_ADMINISTRATORS_T_SQL_SCRIPT = "scripts/populate/two_administrators_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AdministratorDAO administratorDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_ADMINISTRATOR_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_ADMINISTRATOR_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddAdministratorToDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();

        Administrator administrator = new Administrator(1L, "User", "Name");
        administratorDao.save(administrator);

        Optional<Administrator> validAdministrator = administratorDao.get(1L);

        assertThat(validAdministrator.isPresent()).isEqualTo(true);
        assertThat(validAdministrator.get().getUserId()).isEqualTo(administrator.getUserId());
        assertThat(validAdministrator.get().getFirstName()).isEqualTo(administrator.getFirstName());
        assertThat(validAdministrator.get().getLastName()).isEqualTo(administrator.getLastName());

    }

    @Test(expected = AdministratorCreationFailedException.class)
    public void save_shouldThrowError_forInvalidAdministratorObject() {
        Administrator administrator = new Administrator();
        administrator.setFirstName("This string is going to be too long to fit into the database");
        administrator.setLastName("Name");
        administrator.setUserId(1L);

        administratorDao.save(administrator);
    }

    @Test
    public void get_shouldReturnValidAdministrator_forExistingAdministrator() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_ADMINISTRATOR_T_SQL_SCRIPT));
        connection.close();
        Optional<Administrator> validAdministrator = administratorDao.get(1L);

        assertThat(validAdministrator.isPresent()).isEqualTo(true);
        assertThat(validAdministrator.get().getFirstName()).isEqualTo("User");
        assertThat(validAdministrator.get().getLastName()).isEqualTo("Name");
        assertThat(validAdministrator.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void get_shouldReturnInvalidAdministrator_forEmptyDatabase() {
        Optional<Administrator> invalid = administratorDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Administrator> noAdministrators = administratorDao.getAll();

        assertThat(noAdministrators).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfAdministrators_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_ADMINISTRATORS_T_SQL_SCRIPT));

        List<Administrator> administrators = administratorDao.getAll();

        assertThat(administrators).isNotNull().hasSize(2);
        assertThat(administrators.contains(new Administrator(1L, 1L, "User", "Name"))).isTrue();
        assertThat(administrators.contains(new Administrator(2L, 2L, "Second", "User"))).isTrue();

    }

    @Test(expected = AdministratorUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingAdministrator() {
        Administrator notFound = new Administrator();
        notFound.setId(new Random().nextLong());

        administratorDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingAdministrator() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_ADMINISTRATOR_T_SQL_SCRIPT));
        connection.close();

        administratorDao.update(new Administrator(1L,1L, "Different", "Name"));

        Optional<Administrator> updatedAdministrator = administratorDao.get(1L);
        assertThat(updatedAdministrator).isPresent();
        assertThat(updatedAdministrator.get().getFirstName()).isEqualTo("Different");
        assertThat(updatedAdministrator.get().getLastName()).isEqualTo(("Name"));
        assertThat(updatedAdministrator.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void delete_shouldRemoveAdministratorFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_ADMINISTRATOR_T_SQL_SCRIPT));
        connection.close();
        Administrator admin = new Administrator();
        admin.setId(1L);
        administratorDao.delete(admin);

        assertThat(administratorDao.get(1L).isPresent()).isFalse();
        assertThat(administratorDao.getAll()).hasSize(0);
    }

    @Test(expected = AdministratorDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Administrator administrator = new Administrator();
        administrator.setId(new Random().nextLong());

        administratorDao.delete(administrator);
    }

}
