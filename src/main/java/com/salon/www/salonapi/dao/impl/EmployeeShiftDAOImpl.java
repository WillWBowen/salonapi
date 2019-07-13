package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.EmployeeShiftDAO;
import com.salon.www.salonapi.mapper.EmployeeShiftRowMapper;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("ShiftDao")
public class EmployeeShiftDAOImpl implements EmployeeShiftDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeShiftDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<EmployeeShift> get(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                        "SELECT * FROM employee_shifts WHERE id=?",
                        new Object[] {id},
                        new EmployeeShiftRowMapper()
        ));
    }

    @Override
    public List<EmployeeShift> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM employee_shifts", new EmployeeShiftRowMapper()
        );
    }

    @Override
    public void save(EmployeeShift employeeShift) {
        jdbcTemplate.update(
                "INSERT INTO employee_shifts(employees_id, day, start_time, end_time) VALUES(?,?,?,?)",
                employeeShift.getEmployeeId(),
                employeeShift.getDay(),
                employeeShift.getStartTime(),
                employeeShift.getEndTime()
                );
    }

    @Override
    public void update(EmployeeShift employeeShift) {
        jdbcTemplate.update(
                "UPDATE employee_shifts SET employees_id=?, day=?, start_time=?, end_time=? WHERE id=?",
                employeeShift.getEmployeeId(),
                employeeShift.getDay(),
                employeeShift.getStartTime(),
                employeeShift.getEndTime(),
                employeeShift.getId()
        );

    }

    @Override
    public void delete(EmployeeShift employeeShift) {
        jdbcTemplate.update(
                "DELETE FROM employee_shifts WHERE id = ?",
                employeeShift.getId()
        );
    }

    @Override
    public List<EmployeeShift> getAllForEmployee(Employee employee) {
        return jdbcTemplate.query(
                "SELECT * FROM employee_shifts WHERE employees_id=?",
                new Object[] {
                    employee.getId()
                },
                new EmployeeShiftRowMapper()
        );
    }

    @Override
    public Optional<EmployeeShift> getForEmployeeForDay(Employee employee, String day) {
       return Optional.ofNullable(jdbcTemplate.queryForObject(
                        "SELECT * FROM employee_shifts WHERE employees_id=? AND day=?",
                        new Object[] {
                                employee.getId(),
                                day
                        },
                        new EmployeeShiftRowMapper()
                ));
    }
}
