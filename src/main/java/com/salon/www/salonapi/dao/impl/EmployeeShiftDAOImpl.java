package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.EmployeeShiftDAO;
import com.salon.www.salonapi.exception.EmployeeShiftCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeShiftDeletionFailedException;
import com.salon.www.salonapi.exception.EmployeeShiftUpdateFailedException;
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
        String sql = "SELECT * FROM employee_shifts WHERE id=?";
        return jdbcTemplate.query(
                        sql,
                        rs -> rs.next() ? Optional.ofNullable(new EmployeeShiftRowMapper().mapRow(rs, 1))
                                        : Optional.empty(),
                        id
        );
    }

    @Override
    public List<EmployeeShift> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM employee_shifts", new EmployeeShiftRowMapper()
        );
    }

    @Override
    public void save(EmployeeShift employeeShift) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO employee_shifts(employees_id, day, start_time, end_time) VALUES(?,?,?,?)",
                    employeeShift.getEmployeeId(),
                    employeeShift.getDay(),
                    employeeShift.getStartTime(),
                    employeeShift.getEndTime()
            );
        } catch (Exception ex) {
            throw new EmployeeShiftCreationFailedException();
        }
    }

    @Override
    public void update(EmployeeShift employeeShift) {
        if(jdbcTemplate.update(
                "UPDATE employee_shifts SET employees_id=?, day=?, start_time=?, end_time=? WHERE id=?",
                employeeShift.getEmployeeId(),
                employeeShift.getDay(),
                employeeShift.getStartTime(),
                employeeShift.getEndTime(),
                employeeShift.getId()) != 1) {
            throw new EmployeeShiftUpdateFailedException();
        }
    }

    @Override
    public void delete(EmployeeShift employeeShift) {
        if(jdbcTemplate.update(
                "DELETE FROM employee_shifts WHERE id = ?",
                employeeShift.getId()) != 1) {
            throw new EmployeeShiftDeletionFailedException();
        }
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
    public Optional<EmployeeShift> getForEmployeeForDay(Employee employee, int day) {
        String sql = "SELECT * FROM employee_shifts WHERE employees_id=? AND day=?";
        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? Optional.ofNullable(new EmployeeShiftRowMapper().mapRow(rs, 1))
                        : Optional.empty(),
                employee.getId(),
                day
        );
    }
}
