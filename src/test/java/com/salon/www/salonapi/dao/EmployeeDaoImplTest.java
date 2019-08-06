package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.dao.itf.EmployeeDAO;
import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.EmployeeCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeDeletionFailedException;
import com.salon.www.salonapi.exception.EmployeeUpdateFailedException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
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


import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeDaoImplTest {

    private static final String CREATE_EMPLOYEE_T_SQL_SCRIPT = "scripts/create/bookings_t.sql";
    private static final String DROP_EMPLOYEE_T_SQL_SCRIPT = "scripts/drop/bookings_t.sql";
    private static final String POPULATE_ONE_USER_T_SQL_SCRIPT = "scripts/populate/one_user_t.sql";
    private static final String POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT = "scripts/populate/one_employee_t.sql";
    private static final String POPULATE_TWO_EMPLOYEES_T_SQL_SCRIPT = "scripts/populate/two_employees_t.sql";
    private static final String POPULATE_THREE_EMPLOYEE_SHIFTS_T_SQL_SCRIPT = "scripts/populate/three_shifts_t.sql";
    private static final String POPULATE_TWO_BOOKINGS_T_SQL_SCRIPT = "scripts/populate/two_bookings_t.sql";
    private static final String POPULATE_TWO_SKILLS_T_SQL_SCRIPT = "scripts/populate/two_skills_t.sql";
    private static final String POPULATE_EMPLOYEE_SKILL_T_SQL_SCRIPT = "scripts/populate/employee_skills_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EmployeeDAO employeeDao;
    @Autowired
    private SkillDAO skillDao;


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
        connection.close();

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

    @Test
    public void getShiftForDay_shouldYieldEmptyList_forEmptyDatabase() {
        Optional<EmployeeShift> shift = employeeDao.getShiftForDay(1L, 3);
        assertThat(shift.isPresent()).isFalse();
    }

    @Test
    public void getShiftForDay_shouldYieldListOfEmployeeShifts_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_THREE_EMPLOYEE_SHIFTS_T_SQL_SCRIPT));
        connection.close();

        Optional<EmployeeShift> shift = employeeDao.getShiftForDay(1L, 3);

        assertThat(shift.isPresent()).isTrue();
        assertThat(shift.get().getEmployeeId()).isEqualTo(1L);
        assertThat(shift.get().getDay()).isEqualTo(3);
        assertThat(shift.get().getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(shift.get().getEndTime()).isEqualTo(LocalTime.of(19, 30));
    }

    @Test
    public void getBookingsForDate_shouldYieldEmptyList_forEmptyDatabase() {
        Timestamp start1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 0).getTimeInMillis());
        List<Booking> noBookings = employeeDao.getBookingsForDate(1L, start1);

        assertThat(noBookings).isNullOrEmpty();
    }

    @Test
    public void getBookingsForDate_shouldYieldListOfBookings_forNonemptyDatabase() throws SQLException{
        Timestamp start1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 0).getTimeInMillis());
        Timestamp end1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 12, 30).getTimeInMillis());

        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_BOOKINGS_T_SQL_SCRIPT));
        connection.close();

        List<Booking> bookings = employeeDao.getBookingsForDate(1L, start1);


        assertThat(bookings).isNotNull().hasSize(1);
        assertThat(bookings.contains(new Booking(1L,1L,1L, start1, end1, null))).isTrue();

    }

    @Test
    public void addSkill_ShouldAddEmployeeSkillEntryToDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_SKILLS_T_SQL_SCRIPT));
        connection.close();
        Employee employee = new Employee(1L, 1L, "first", "last");
        Skill skill = new Skill(1L,"manicure", 20);

        List<Skill> skills = skillDao.getForEmployee(1L);
        assertThat(skills).isNotNull().hasSize(0);

        employeeDao.addSkill(employee, skill);

        skills = skillDao.getForEmployee(1L);
        assertThat(skills).isNotNull().hasSize(1);

        Skill result = skills.get(0);
        assertThat(result).hasFieldOrPropertyWithValue("name", "manicure");
        assertThat(result).hasFieldOrPropertyWithValue("price", 20);
    }

    @Test
    public void removeSkill_ShouldRemoveEmployeeSkillEntryFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_EMPLOYEE_SKILL_T_SQL_SCRIPT));
        connection.close();
        Employee employee = new Employee(1L, 1L, "first", "last");
        Skill skill = new Skill(2L,"pedicure", 30);

        List<Skill> skills = skillDao.getForEmployee(1L);
        assertThat(skills).isNotNull().hasSize(1);

        Skill result = skills.get(0);
        assertThat(result).hasFieldOrPropertyWithValue("name", "pedicure");
        assertThat(result).hasFieldOrPropertyWithValue("price", 30);


        employeeDao.removeSkill(employee, skill);

        skills = skillDao.getForEmployee(1L);
        assertThat(skills).isNotNull().hasSize(0);
    }
}
