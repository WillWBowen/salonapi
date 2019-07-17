package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.exception.EmployeeShiftCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeShiftDeletionFailedException;
import com.salon.www.salonapi.exception.EmployeeShiftUpdateFailedException;
import com.salon.www.salonapi.model.EmployeeShift;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeShiftDaoImplTest {

    private static final String CREATE_EMPLOYEE_SHIFT_T_SQL_SCRIPT = "scripts/create/shifts_t.sql";
    private static final String DROP_EMPLOYEE_SHIFT_T_SQL_SCRIPT = "scripts/drop/shifts_t.sql";
    private static final String POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT = "scripts/populate/one_employee_t.sql";
    private static final String POPULATE_ONE_EMPLOYEE_SHIFT_T_SQL_SCRIPT = "scripts/populate/one_shift_t.sql";
    private static final String POPULATE_TWO_EMPLOYEE_SHIFTS_T_SQL_SCRIPT = "scripts/populate/two_shifts_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EmployeeShiftDAO shiftDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_EMPLOYEE_SHIFT_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_EMPLOYEE_SHIFT_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddEmployeeShiftToDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_T_SQL_SCRIPT));
        connection.close();

        EmployeeShift shift = new EmployeeShift(1L, 2, LocalTime.of(8, 30), LocalTime.of(17, 0));
        shiftDao.save(shift);

        Optional<EmployeeShift> validEmployeeShift = shiftDao.get(1L);

        assertThat(validEmployeeShift.isPresent()).isEqualTo(true);
        assertThat(validEmployeeShift.get().getDay()).isEqualTo(shift.getDay());
        assertThat(validEmployeeShift.get().getEmployeeId()).isEqualTo(shift.getEmployeeId());
        assertThat(validEmployeeShift.get().getStartTime()).isEqualTo(shift.getStartTime());
        assertThat(validEmployeeShift.get().getEndTime()).isEqualTo((shift.getEndTime()));

    }

    @Test(expected = EmployeeShiftCreationFailedException.class)
    public void save_shouldThrowError_forInvalidEmployeeShiftObject() {
        EmployeeShift shift = new EmployeeShift();

        shiftDao.save(shift);
    }

    @Test
    public void get_shouldReturnValidEmployeeShift_forExistingEmployeeShift() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_SHIFT_T_SQL_SCRIPT));
        connection.close();
        Optional<EmployeeShift> validEmployeeShift = shiftDao.get(1L);

        assertThat(validEmployeeShift.isPresent()).isEqualTo(true);
        assertThat(validEmployeeShift.get().getEmployeeId()).isEqualTo(1L);
        assertThat(validEmployeeShift.get().getDay()).isEqualTo(2);
        assertThat(validEmployeeShift.get().getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(validEmployeeShift.get().getEndTime()).isEqualTo(LocalTime.of(17, 30));
    }

    @Test
    public void get_shouldReturnInvalidEmployeeShift_forEmptyDatabase() {
        Optional<EmployeeShift> invalid = shiftDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<EmployeeShift> noEmployeeShifts = shiftDao.getAll();

        assertThat(noEmployeeShifts).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfEmployeeShifts_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_EMPLOYEE_SHIFTS_T_SQL_SCRIPT));

        List<EmployeeShift> shifts = shiftDao.getAll();

        assertThat(shifts).isNotNull().hasSize(2);
        assertThat(shifts.contains(new EmployeeShift(1L,1L, 2, LocalTime.of(9, 0), LocalTime.of(17,30)))).isTrue();
        assertThat(shifts.contains(new EmployeeShift(2L,1L, 3, LocalTime.of(10, 0), LocalTime.of(19, 30)))).isTrue();

    }

    @Test(expected = EmployeeShiftUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingEmployeeShift() {
        EmployeeShift notFound = new EmployeeShift();
        notFound.setId(new Random().nextLong());

        shiftDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingEmployeeShift() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_SHIFT_T_SQL_SCRIPT));
        connection.close();

        shiftDao.update(new EmployeeShift(1L,1L, 5, LocalTime.of(8, 0), LocalTime.of(12,30)));

        Optional<EmployeeShift> updatedEmployeeShift = shiftDao.get(1L);
        assertThat(updatedEmployeeShift.isPresent()).isEqualTo(true);
        assertThat(updatedEmployeeShift.get().getEmployeeId()).isEqualTo(1L);
        assertThat(updatedEmployeeShift.get().getDay()).isEqualTo(5);
        assertThat(updatedEmployeeShift.get().getStartTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(updatedEmployeeShift.get().getEndTime()).isEqualTo(LocalTime.of(12, 30));
    }

    @Test
    public void delete_shouldRemoveEmployeeShiftFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_EMPLOYEE_SHIFT_T_SQL_SCRIPT));
        connection.close();
        EmployeeShift shift = new EmployeeShift();
        shift.setId(1L);
        shiftDao.delete(shift);

        assertThat(shiftDao.get(1L).isPresent()).isFalse();
        assertThat(shiftDao.getAll()).hasSize(0);
    }

    @Test(expected = EmployeeShiftDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        EmployeeShift shift = new EmployeeShift();
        shift.setId(new Random().nextLong());

        shiftDao.delete(shift);
    }
}
