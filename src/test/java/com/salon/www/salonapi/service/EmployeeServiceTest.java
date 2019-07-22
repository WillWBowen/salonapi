package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.EmployeeDAO;
import com.salon.www.salonapi.exception.EmployeeCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeNotFoundException;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.service.impl.EmployeeServiceImpl;
import com.salon.www.salonapi.service.itf.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
    private EmployeeDAO employeeDao;

    @Before
    public void setUp() {
        this.employeeService = new EmployeeServiceImpl(employeeDao);
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


}
