package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.exception.RoleCreationFailedException;
import com.salon.www.salonapi.exception.RoleDeletionFailedException;
import com.salon.www.salonapi.exception.RoleUpdateFailedException;
import com.salon.www.salonapi.model.Role;
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
public class RoleDaoImplTest {

    private static final String CREATE_ROLE_T_SQL_SCRIPT = "scripts/create/roles_t.sql";
    private static final String DROP_ROLE_T_SQL_SCRIPT = "scripts/drop/roles_t.sql";
    private static final String POPULATE_ONE_ROLE_T_SQL_SCRIPT = "scripts/populate/one_role_t.sql";
    private static final String POPULATE_TWO_ROLES_T_SQL_SCRIPT = "scripts/populate/two_roles_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoleDAO roleDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_ROLE_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_ROLE_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddRoleToDatabase() {
        Role role = new Role("customer");
        roleDao.save(role);

        Optional<Role> validRole = roleDao.get(1L);

        assertThat(validRole.isPresent()).isEqualTo(true);
        assertThat(validRole.get().getRole()).isEqualTo(role.getRole());
    }

    @Test(expected = RoleCreationFailedException.class)
    public void save_shouldThrowError_forInvalidRoleObject() {
        Role role = new Role();
        role.setRole("This string is going to be too long to fit into the database");

        roleDao.save(role);
    }

    @Test
    public void get_shouldReturnValidRole_forExistingRole() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_ROLE_T_SQL_SCRIPT));
        connection.close();
        Optional<Role> validRole = roleDao.get(1L);

        assertThat(validRole.isPresent()).isEqualTo(true);
        assertThat(validRole.get().getRole()).isEqualTo("customer");
    }

    @Test
    public void get_shouldReturnInvalidRole_forEmptyDatabase() {
        Optional<Role> invalid = roleDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Role> noRoles = roleDao.getAll();

        assertThat(noRoles).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfRoles_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_ROLES_T_SQL_SCRIPT));

        List<Role> roles = roleDao.getAll();

        assertThat(roles).isNotNull().hasSize(2);
        assertThat(roles.contains(new Role(1L, "customer"))).isTrue();
        assertThat(roles.contains(new Role(2L, "employee"))).isTrue();

    }

    @Test(expected = RoleUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingRole() {
        Role notFound = new Role();
        notFound.setId(new Random().nextLong());

        roleDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingRole() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_ROLE_T_SQL_SCRIPT));
        connection.close();

        roleDao.update(new Role(1L,"employee"));

        Optional<Role> updatedRole = roleDao.get(1L);
        assertThat(updatedRole).isPresent();
        assertThat(updatedRole.get().getRole()).isEqualTo("employee");
    }

    @Test
    public void delete_shouldRemoveRoleFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_ROLE_T_SQL_SCRIPT));
        connection.close();

        roleDao.delete(new Role(1L, ""));

        assertThat(roleDao.get(1L).isPresent()).isFalse();
        assertThat(roleDao.getAll()).hasSize(0);
    }

    @Test(expected = RoleDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Role role = new Role();
        role.setId(new Random().nextLong());

        roleDao.delete(role);
    }

}
