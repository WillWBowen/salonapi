package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.exception.EmployeeCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeDeletionFailedException;
import com.salon.www.salonapi.exception.EmployeeUpdateFailedException;
import com.salon.www.salonapi.model.Employee;
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
public class EmployeeDaoImplTest {

    private static final String CREATE_EMPLOYEE_T_SQL_SCRIPT = "scripts/create/employees_t.sql";
    private static final String DROP_EMPLOYEE_T_SQL_SCRIPT = "scripts/drop/employees_t.sql";
    private static final String POPULATE_ONE_USER_T_SQL_SCRIPT = "scripts/populate/one_user_t.sql";
    private static final String POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT = "scripts/populate/one_employee_t.sql";
    private static final String POPULATE_TWO_EMPLOYEES_T_SQL_SCRIPT = "scripts/populate/two_employees_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EmployeeDAO employeeDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_EMPLOYEE_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_EMPLOYEE_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddEmployeeToDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();

        Employee employee = new Employee(1L, "User", "Name");
        employeeDao.save(employee);

        Optional<Employee> validEmployee = employeeDao.get(1L);

        assertThat(validEmployee.isPresent()).isEqualTo(true);
        assertThat(validEmployee.get().getUserId()).isEqualTo(employee.getUserId());
        assertThat(validEmployee.get().getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(validEmployee.get().getLastName()).isEqualTo(employee.getLastName());

    }

    @Test(expected = EmployeeCreationFailedException.class)
    public void save_shouldThrowError_forInvalidEmployeeObject() {
        Employee employee = new Employee();
        employee.setFirstName("This string is going to be too long to fit into the database");
        employee.setLastName("Name");
        employee.setUserId(1L);

        employeeDao.save(employee);
    }

    @Test
    public void get_shouldReturnValidEmployee_forExistingEmployee() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT));
        connection.close();
        Optional<Employee> validEmployee = employeeDao.get(1L);

        assertThat(validEmployee.isPresent()).isEqualTo(true);
        assertThat(validEmployee.get().getFirstName()).isEqualTo("User");
        assertThat(validEmployee.get().getLastName()).isEqualTo("Name");
        assertThat(validEmployee.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void get_shouldReturnInvalidEmployee_forEmptyDatabase() {
        Optional<Employee> invalid = employeeDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Employee> noEmployees = employeeDao.getAll();

        assertThat(noEmployees).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfEmployees_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_EMPLOYEES_T_SQL_SCRIPT));

        List<Employee> employees = employeeDao.getAll();

        assertThat(employees).isNotNull().hasSize(2);
        assertThat(employees.contains(new Employee(1L, 1L, "User", "Name"))).isTrue();
        assertThat(employees.contains(new Employee(2L, 2L, "Second", "User"))).isTrue();

    }

    @Test(expected = EmployeeUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingEmployee() {
        Employee notFound = new Employee();
        notFound.setId(new Random().nextLong());

        employeeDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingEmployee() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT));
        connection.close();

        employeeDao.update(new Employee(1L,1L, "Different", "Name"));

        Optional<Employee> updatedEmployee = employeeDao.get(1L);
        assertThat(updatedEmployee).isPresent();
        assertThat(updatedEmployee.get().getFirstName()).isEqualTo("Different");
        assertThat(updatedEmployee.get().getLastName()).isEqualTo(("Name"));
        assertThat(updatedEmployee.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void delete_shouldRemoveEmployeeFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT));
        connection.close();
        Employee employee = new Employee();
        employee.setId(1L);
        employeeDao.delete(employee);

        assertThat(employeeDao.get(1L).isPresent()).isFalse();
        assertThat(employeeDao.getAll()).hasSize(0);
    }

    @Test(expected = EmployeeDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Employee employee = new Employee();
        employee.setId(new Random().nextLong());

        employeeDao.delete(employee);
    }

}
