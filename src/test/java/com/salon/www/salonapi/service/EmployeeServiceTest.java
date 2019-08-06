package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.EmployeeDAO;
import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.EmployeeCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.impl.EmployeeServiceImpl;
import com.salon.www.salonapi.service.itf.BookingService;
import com.salon.www.salonapi.service.itf.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Mock
    private BookingService bookingService;
    @Mock
    private EmployeeDAO employeeDao;
    @Mock
    private SkillDAO skillDao;

    @InjectMocks
    @Spy
    private EmployeeServiceImpl spyService;

    private static final Timestamp START_1 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 11, 0).getTimeInMillis());
    private static final Timestamp END_1 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 12, 0).getTimeInMillis());
    private static final Timestamp START_2 = new Timestamp(new GregorianCalendar(2019, Calendar.SEPTEMBER, 30, 9, 30).getTimeInMillis());
    private static final Timestamp END_2 = new Timestamp(new GregorianCalendar(2019, Calendar.SEPTEMBER, 30, 11, 15).getTimeInMillis());

    @Before
    public void setUp() {
        this.employeeService = new EmployeeServiceImpl(employeeDao, skillDao, bookingService);
    }

    @Test
    public void getEmployees_should_returnNull_inNoEmployeesInDatabase() {
        List<Employee> employees = employeeService.getEmployees();
        then(employeeDao).should(times(1)).getAll();
        assertThat(employees).isNullOrEmpty();
    }

    @Test
    public void getEmployees_shouldReturnListOfEmployees_forNonEmptyDatabase() {
        Employee first = new Employee(1L, 1L, "first", "name");
        Employee second = new Employee(2L, 4L, "last", "name");

        given(employeeDao.getAll()).willReturn(Arrays.asList(first, second));

        List<Employee> employees = employeeService.getEmployees();

        then(employeeDao).should(times(1)).getAll();
        assertThat(employees).contains(first);
        assertThat(employees).contains(second);
    }

    @Test
    public void updateEmployee_shouldVerifyThatEmployee_exists() {
        Employee employee = new Employee();
        employee.setId(10L);
        given(employeeDao.get(anyLong())).willReturn(Optional.of(employee));
        employeeService.updateEmployee(employee);
        then(employeeDao).should(times(1)).get(10L);
    }

    @Test
    public void updateEmployee_shouldCallDAOUpdate_ifEmployeeExists() {
        Employee employee = new Employee(1L, 1L, "first", "name");
        given(employeeDao.get(1L)).willReturn(Optional.of(employee));
        doNothing().when(employeeDao).update(employee);

        employeeService.updateEmployee(employee);

        then(employeeDao).should(times(1)).update(employee);
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void updateEmployee_shouldThrowError_ifEmployeeDoesNotExist() {
        Employee employee = new Employee();
        employee.setId(1L);
        given(employeeDao.get(1L)).willReturn(Optional.empty());

        employeeService.updateEmployee(employee);
        then(employeeDao).should(times(1)).get(1L);
        then(employeeDao).should(times(0)).update(employee);
    }

    @Test
    public void getEmployee_returnsNull_forEmptyOptional() {
        given(employeeDao.get(anyLong())).willReturn(Optional.empty());

        Employee employee = employeeService.getEmployee(14L);

        then(employeeDao).should(times(1)).get(14L);
        assertThat(employee).isNull();
    }

    @Test
    public void getEmployee_returnsEmployee_forNonEmptyOptional() {
        Employee employee = new Employee(1L, 1L, "first", "name");
        given(employeeDao.get(anyLong())).willReturn(Optional.of(employee));

        Employee storedEmployee = employeeService.getEmployee(14L);

        then(employeeDao).should(times(1)).get(14L);
        assertThat(storedEmployee).isNotNull();
        assertThat(storedEmployee).isEqualTo(employee);
    }

    @Test
    public void createEmployee_callsDAOSaveMethod() {
        Employee employee = new Employee(12L, Calendar.WEDNESDAY, null, null);
        doNothing().when(employeeDao).save(any());

        employeeService.createEmployee(employee);
        then(employeeDao).should(times(1)).save(employee);
    }

    @Test(expected = EmployeeCreationFailedException.class)
    public void createEmployee_shouldThrowEmployeeCreationFailedException() {
        Employee employee = new Employee(12L, Calendar.WEDNESDAY, null, null);
        doThrow(EmployeeCreationFailedException.class).when(employeeDao).save(any());

        employeeService.createEmployee(employee);
    }

    @Test
    public void getEmployeeShiftForDay_returnsNull_forEmptyOptional() {
        given(employeeDao.getShiftForDay(anyLong(), anyInt())).willReturn(Optional.empty());

        EmployeeShift shift = employeeService.getEmployeeShiftForDay(14L, 1);

        then(employeeDao).should(times(1)).getShiftForDay(14L, 1);
        assertThat(shift).isNull();
    }

    @Test
    public void getEmployeeShiftForDay_returnsShift_forNonEmptyOptional() {
        EmployeeShift shift = new EmployeeShift(14L, 12L, Calendar.WEDNESDAY, null, null);
        given(employeeDao.getShiftForDay(anyLong(), anyInt())).willReturn(Optional.of(shift));

        EmployeeShift storedShift = employeeService.getEmployeeShiftForDay(14L, 1);

        then(employeeDao).should(times(1)).getShiftForDay(14L, 1);
        assertThat(storedShift).isNotNull();
        assertThat(storedShift).isEqualTo(shift);
    }

    @Test
    public void getEmployeeBookingsForDate_should_returnNull_ifNoBookingsInDatabase() {
        given(employeeDao.getBookingsForDate(anyLong(), any())).willReturn(null);

        Timestamp time = new Timestamp(new GregorianCalendar(2019, 7, 3).getTimeInMillis());
        List<Booking> bookings = employeeService.getEmployeeBookingsForDate(13L, time);
        then(employeeDao).should(times(1)).getBookingsForDate(13L, time);
        assertThat(bookings).isNullOrEmpty();
    }

    @Test
    public void getEmployeeBookingsForDate_shouldReturnListOfBookings_forNonEmptyDatabase() {
        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);

        given(employeeDao.getBookingsForDate(anyLong(), any())).willReturn(Arrays.asList(first, second));

        List<Booking> bookings = employeeService.getEmployeeBookingsForDate(14L, START_1);

        then(employeeDao).should(times(1)).getBookingsForDate(14L, START_1);
        assertThat(bookings).contains(first);
        assertThat(bookings).contains(second);
    }

    @Test
    public void employeeIsAvailable_returnsFalseForNoShiftOnBookingDay() {
        doReturn(null).when(spyService).getEmployeeShiftForDay(anyLong(), anyInt());
        Boolean isAvailable = spyService.employeeIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getEmployeeShiftForDay(12L, 6);
        assertThat(isAvailable).isFalse();
    }

    @Test
    public void employeeIsAvailable_returnsFalseForShiftStartsAfterBooking() {
        EmployeeShift shift = new EmployeeShift(14L, 12L, Calendar.WEDNESDAY,LocalTime.of(16, 0), null);
        doReturn(shift).when(spyService).getEmployeeShiftForDay(anyLong(), anyInt());
        Boolean isAvailable = spyService.employeeIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getEmployeeShiftForDay(12L, 6);
        assertThat(isAvailable).isFalse();
    }

    @Test
    public void employeeIsAvailable_returnsFalseForShiftEndsBeforeEndOfBooking() {
        EmployeeShift shift = new EmployeeShift(14L, 12L, Calendar.WEDNESDAY,LocalTime.of(8, 0), LocalTime.of(9,0));
        doReturn(shift).when(spyService).getEmployeeShiftForDay(anyLong(), anyInt());
        Boolean isAvailable = spyService.employeeIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getEmployeeShiftForDay(12L, 6);
        assertThat(isAvailable).isFalse();
    }

    @Test
    public void employeeIsAvailable_returnsFalseForOverLappingBookings() {
        EmployeeShift shift = new EmployeeShift(14L, 12L, Calendar.WEDNESDAY,LocalTime.of(8, 0), LocalTime.of(17,0));

        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);
        List<Booking> bookings = Arrays.asList(first, second);
        doReturn(bookings).when(spyService).getEmployeeBookingsForDate(anyLong(), any());
        doReturn(shift).when(spyService).getEmployeeShiftForDay(anyLong(), anyInt());
        doReturn(true).when(bookingService).bookingTimeHasConflict(any(), any(), any());

        Boolean isAvailable = spyService.employeeIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getEmployeeShiftForDay(12L, 6);
        then(spyService).should(times(1)).getEmployeeBookingsForDate(12L, START_1);
        then(bookingService).should(times(1)).bookingTimeHasConflict(bookings, START_1, END_1);
        assertThat(isAvailable).isFalse();
    }

    @Test
    public void employeeIsAvailable_returnsTrueForValidBookings() {
        EmployeeShift shift = new EmployeeShift(14L, 12L, Calendar.WEDNESDAY,LocalTime.of(8, 0), LocalTime.of(17,0));

        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);
        List<Booking> bookings = Arrays.asList(first, second);
        doReturn(bookings).when(spyService).getEmployeeBookingsForDate(anyLong(), any());
        doReturn(shift).when(spyService).getEmployeeShiftForDay(anyLong(), anyInt());
        doReturn(false).when(bookingService).bookingTimeHasConflict(any(), any(), any());

        Boolean isAvailable = spyService.employeeIsAvailable(12L, START_1, END_1);

        then(spyService).should(times(1)).getEmployeeShiftForDay(12L, 6);
        then(spyService).should(times(1)).getEmployeeBookingsForDate(12L, START_1);
        then(bookingService).should(times(1)).bookingTimeHasConflict(bookings, START_1, END_1);
        assertThat(isAvailable).isTrue();
    }

    @Test
    public void getEmployeeSkills_returnsListOfSkills() {
        Skill first = new Skill(1L, "manicure", 20);
        Skill second = new Skill(2L, "pedicure", 30);
        given(skillDao.getForEmployee(anyLong())).willReturn(Arrays.asList(first, second));

        List<Skill> skills = employeeService.getEmployeeSkills(12L);

        then(skillDao).should(times(1)).getForEmployee(12L);
        assertThat(skills.size()).isEqualTo(2);
        assertThat((skills.contains(first))).isTrue();
        assertThat((skills.contains(second))).isTrue();
    }

    @Test
    public void getEmployeeSkills_returnsNull_forEmptyDatabase() {
        given(skillDao.getForEmployee(anyLong())).willReturn(null);

        List<Skill> skills = employeeService.getEmployeeSkills(12L);

        then(skillDao).should(times(1)).getForEmployee(12L);
        assertThat(skills).isNullOrEmpty();
    }

    @Test
    public void employeeHasSkills_returnsTrueIfEmployeeHasAllSkills() {
        Skill first = new Skill(1L, "manicure", 20);
        Skill second = new Skill(2L, "pedicure", 30);
        doReturn(Arrays.asList(first, second)).when(spyService).getEmployeeSkills(anyLong());

        Boolean hasSkills = spyService.employeeHasSkills(14L, Arrays.asList(second));

        assertThat(hasSkills).isTrue();
    }

    @Test
    public void employeeHasSkills_returnsFalseIfEmployeeDoesNotHaveAllSkills() {
        Skill first = new Skill(1L, "manicure", 20);
        Skill second = new Skill(2L, "pedicure", 30);
        doReturn(Arrays.asList(first)).when(spyService).getEmployeeSkills(anyLong());

        Boolean hasSkills = spyService.employeeHasSkills(14L, Arrays.asList(first, second));

        assertThat(hasSkills).isFalse();
    }
}
