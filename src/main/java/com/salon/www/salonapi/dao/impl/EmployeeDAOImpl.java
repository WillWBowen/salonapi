package com.salon.www.salonapi.dao.impl;

import com.salon.www.salonapi.dao.itf.EmployeeDAO;
import com.salon.www.salonapi.exception.EmployeeCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeDeletionFailedException;
import com.salon.www.salonapi.exception.EmployeeUpdateFailedException;
import com.salon.www.salonapi.mapper.EmployeeRowMapper;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("employeeDao")
public class EmployeeDAOImpl implements EmployeeDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Employee> get(long id) {
        String sql = "SELECT * FROM employees WHERE id=?";
        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? Optional.ofNullable(new EmployeeRowMapper().mapRow(rs, 1)) : Optional.empty(),
                id);
    }

    @Override
    public List<Employee> getAll(){
        return jdbcTemplate.query(
                "SELECT * FROM employees", new EmployeeRowMapper()
        );
    }

    @Override
    public void save(Employee employee) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO employees(users_id, first_name, last_name, position, status) VALUES(?,?,?,?,?)",
                    employee.getUserId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getPosition(),
                    employee.getStatus()
            );
        } catch(Exception ex) {
            throw new EmployeeCreationFailedException();
        }
    }

    @Override
    public void update(Employee employee) {
        if(jdbcTemplate.update(
                "UPDATE employees SET first_name=?, last_name=?, position=?, status=? WHERE id=?",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPosition(),
                employee.getStatus(),
                employee.getId()) != 1) {
            throw new EmployeeUpdateFailedException();
        }
    }

    @Override
    public void delete(Employee employee) {
        if(jdbcTemplate.update(
                "DELETE FROM employees WHERE id = ?",
                employee.getId()) != 1) {
            throw new EmployeeDeletionFailedException();
        }
    }

    @Override
    public void addSkill(Employee employee, Skill skill) {
        jdbcTemplate.update("INSERT INTO employees_x_skills(employees_id, skills_id) VALUES (?,?)",
                employee.getId(),
                skill.getId()
        );
    }

    @Override
    public void removeSkill(Employee employee, Skill skill) {
        jdbcTemplate.update("DELETE FROM employees_x_skills WHERE employees_id=? AND skills_id=?",
                employee.getId(),
                skill.getId()
        );
    }
}
