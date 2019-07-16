package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.exception.*;
import com.salon.www.salonapi.model.UserDto;
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
public class UserDaoImplTest {

    private static final String CREATE_USER_T_SQL_SCRIPT = "scripts/create/users_t.sql";
    private static final String DROP_USER_T_SQL_SCRIPT = "scripts/drop/users_t.sql";
    private static final String POPULATE_ONE_USER_T_SQL_SCRIPT = "scripts/populate/one_user_t.sql";
    private static final String POPULATE_TWO_USERS_T_SQL_SCRIPT = "scripts/populate/two_users_t.sql";

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
    public void save_shouldAddUserDtoToDatabase() {
        UserDto user = new UserDto("username", "password");
        userDao.save(user);

        Optional<UserDto> validUserDto = userDao.get(1L);

        assertThat(validUserDto.isPresent()).isEqualTo(true);
        assertThat(validUserDto.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test(expected = UserCreationFailedException.class)
    public void save_shouldThrowError_forInvalidUserDtoObject() {
        UserDto user = new UserDto();
        user.setUsername("This string is going to be too long to fit into the database");

        userDao.save(user);
    }

    @Test
    public void get_shouldReturnValidUserDto_forExistingUserDto() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();
        Optional<UserDto> validUser = userDao.get(1L);

        assertThat(validUser.isPresent()).isEqualTo(true);
        assertThat(validUser.get().getUsername()).isEqualTo("username");
        assertThat(validUser.get().getPassword()).isEqualTo("REDACTED");
    }

    @Test
    public void get_shouldReturnInvalidUserDto_forEmptyDatabase() {
        Optional<UserDto> invalid = userDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<UserDto> noUsers = userDao.getAll();

        assertThat(noUsers).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfUserDtos_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_USERS_T_SQL_SCRIPT));

        List<UserDto> users = userDao.getAll();

        assertThat(users).isNotNull().hasSize(2);
        assertThat(users.contains(new UserDto(1L, "username", "REDACTED"))).isTrue();
        assertThat(users.contains(new UserDto(2L, "other", "REDACTED"))).isTrue();

    }

    @Test(expected = UserUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingUser() {
        UserDto notFound = new UserDto();
        notFound.setId(new Random().nextLong());

        userDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingUser() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();

        userDao.update(new UserDto(1L,"differentname", "newPassword"));

        Optional<UserDto> updatedUser = userDao.get(1L);
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getUsername()).isEqualTo("differentname");
        assertThat(updatedUser.get().getPassword()).isEqualTo("REDACTED");
    }

    @Test
    public void delete_shouldRemoveUserFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();
        UserDto user = new UserDto();
        user.setId(1L);
        userDao.delete(user);

        assertThat(userDao.get(1L).isPresent()).isFalse();
        assertThat(userDao.getAll()).hasSize(0);
    }

    @Test(expected = UserDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        UserDto user = new UserDto();
        user.setId(new Random().nextLong());

        userDao.delete(user);
    }

}
