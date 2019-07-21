package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.dao.itf.UserDAO;
import com.salon.www.salonapi.exception.*;
import com.salon.www.salonapi.model.Role;
import com.salon.www.salonapi.model.security.Authority;
import com.salon.www.salonapi.model.security.AuthorityName;
import com.salon.www.salonapi.model.security.User;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoImplTest {

    private static final String CREATE_USER_T_SQL_SCRIPT = "scripts/create/users_t.sql";
    private static final String DROP_USER_T_SQL_SCRIPT = "scripts/drop/users_t.sql";
    private static final String POPULATE_ONE_USER_T_SQL_SCRIPT = "scripts/populate/one_user_t.sql";
    private static final String POPULATE_TWO_USERS_T_SQL_SCRIPT = "scripts/populate/two_users_t.sql";
    private static final String POPULATE_USER_WITH_ROLES_T_SQL_SCRIPT = "scripts/populate/one_user_two_roles_t.sql";
    private static final String POPULATE_ONE_USER_TWO_AUTHORITIES_T_SQL_SCRIPT = "scripts/populate/one_user_two_authorities_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDAO userDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_USER_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_USER_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddUserToDatabase() {
        User user = new User("username", "password", "test@email.com");
        userDao.save(user);

        Optional<User> validUser = userDao.get(1L);
        log.debug(validUser);
        assertThat(validUser.isPresent()).isEqualTo(true);
        assertThat(validUser.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(validUser.get().getPassword()).isEqualTo(user.getPassword());
        assertThat(validUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test(expected = UserCreationFailedException.class)
    public void save_shouldThrowError_forInvalidUserObject() {
        User user = new User();
        user.setUsername("This string is going to be too long to fit into the database");

        userDao.save(user);
    }

    @Test
    public void get_shouldReturnValidUser_forExistingUser() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();
        Optional<User> validUser = userDao.get(1L);

        assertThat(validUser.isPresent()).isEqualTo(true);
        assertThat(validUser.get().getUsername()).isEqualTo("username");
        assertThat(validUser.get().getPassword()).isEqualTo("password");
    }

    @Test
    public void findByUsername_shouldReturnInvalidUser_forEmptyDatabase() {
        Optional<User> invalid = userDao.findByUsername("RandomUserName");

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void findByUsername_shouldReturnValidUser_forExistingUser() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();
        Optional<User> validUser = userDao.findByUsername("username");

        assertThat(validUser.isPresent()).isEqualTo(true);
        assertThat(validUser.get().getUsername()).isEqualTo("username");
        assertThat(validUser.get().getPassword()).isEqualTo("password");
    }

    @Test
    public void get_shouldReturnInvalidUser_forEmptyDatabase() {
        Optional<User> invalid = userDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<User> noUsers = userDao.getAll();

        assertThat(noUsers).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfUsers_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_USERS_T_SQL_SCRIPT));

        List<User> users = userDao.getAll();

        assertThat(users).isNotNull().hasSize(2);
        assertThat(users.contains(new User(1L,"username", "password", "test@email.com", Boolean.TRUE, null, null))).isTrue();
        assertThat(users.contains(new User(2L, "other", "different", "second@test.com", Boolean.TRUE, null, null))).isTrue();

    }

    @Test
    public void getUserRoles_shouldYieldEmptyList_forUserWithNoRoles() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();

        User user = new User();
        user.setId(1L);
        List<Role> noRoles = userDao.getUserRoles(user);

        assertThat(noRoles).isNullOrEmpty();
    }

    @Test
    public void getUserRoles_shouldYieldListOfRoles_forUserWithRoles() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_USER_WITH_ROLES_T_SQL_SCRIPT));
        connection.close();

        User user = new User();
        user.setId(1L);
        List<Role> userRoles = userDao.getUserRoles(user);

        assertThat(userRoles).isNotNull().hasSize(2);
        assertThat(userRoles.contains(new Role(1L, "customer"))).isTrue();
        assertThat(userRoles.contains(new Role(2L, "employee"))).isTrue();

    }

    @Test(expected = UserUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingUser() {
        User notFound = new User();
        notFound.setId(new Random().nextLong());

        userDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingUser() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();

        userDao.update(new User(1L,"differentname", "newPassword", "test@email.com"));

        Optional<User> updatedUser = userDao.get(1L);
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getUsername()).isEqualTo("differentname");
        assertThat(updatedUser.get().getPassword()).isEqualTo("newPassword");
        assertThat(updatedUser.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    public void delete_shouldRemoveUserFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();
        User user = new User();
        user.setId(1L);
        userDao.delete(user);

        assertThat(userDao.get(1L).isPresent()).isFalse();
        assertThat(userDao.getAll()).hasSize(0);
    }

    @Test(expected = UserDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        User user = new User();
        user.setId(new Random().nextLong());

        userDao.delete(user);
    }

    @Test
    public void getAuthoritiesForUser_shouldReturnListOfAuthorities() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_TWO_AUTHORITIES_T_SQL_SCRIPT));
        connection.close();
        User user = new User();
        user.setId(1L);

        List<Authority> authorities = userDao.getCapabilitiesForUser(user);

        assertThat(authorities).isNotNull().hasSize(2);
        assertThat(authorities.contains(new Authority(1L, AuthorityName.ROLE_USER, null))).isTrue();
        assertThat(authorities.contains(new Authority(2L, AuthorityName.ROLE_ADMIN, null))).isTrue();


    }
}
