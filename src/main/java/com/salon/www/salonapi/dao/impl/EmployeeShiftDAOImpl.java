package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.EmployeeShiftDAO;
import com.salon.www.salonapi.mapper.EmployeeShiftRowMapper;
import com.salon.www.salonapi.model.EmployeeShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class EmployeeShiftDAOImpl implements EmployeeShiftDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<EmployeeShift> get(long id) {
        Optional<EmployeeShift> employeeShift =
                (Optional<EmployeeShift>) jdbcTemplate.queryForObject(
                        "SELECT * FROM employee_shifts WHERE id=?",
                        new Object[] {id},
                        new EmployeeShiftRowMapper()

                );

        return employeeShift;
    }

    @Override
    public List<EmployeeShift> getAll() {
        List<EmployeeShift> employeeShifts = jdbcTemplate.query(
                "SELECT * FROM employee_shifts", new EmployeeShiftRowMapper()
        );

        return employeeShifts;
    }

    @Override
    public void save(EmployeeShift employeeShift) {
        jdbcTemplate.update(
                "INSERT INTO employee_shifts(employees_id, day, start_time, end_time) VALUES(?,?,?,?)",
                new Object[] {
                        employeeShift.getEmployeeId(),
                        employeeShift.getDay(),
                        employeeShift.getStartTime(),
                        employeeShift.getEndTime()
                });
    }

    @Override
    public void update(EmployeeShift employeeShift, String[] params) {
        jdbcTemplate.update(
                "UPDATE employee_shifts SET employees_id=?, day=?, start_time=?, end_time=? WHERE id=?",
                new Object[] {
                        params[0],
                        params[1],
                        params[2],
                        params[3],
                        employeeShift.getId()
                }
        );

    }

    @Override
    public void delete(EmployeeShift employeeShift) {
        jdbcTemplate.update(
                "DELETE FROM employee_shifts WHERE id = ?",
                new Object[] {employeeShift.getId()}
        );
    }
}
