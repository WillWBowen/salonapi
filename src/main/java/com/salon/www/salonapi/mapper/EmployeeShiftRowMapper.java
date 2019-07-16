package com.salon.www.salonapi.mapper;

import com.salon.www.salonapi.model.EmployeeShift;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeShiftRowMapper implements RowMapper<EmployeeShift> {
    public EmployeeShift mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeShift employeeShift = new EmployeeShift();
        employeeShift.setId(rs.getLong("id"));
        employeeShift.setDay(rs.getString("day"));
        employeeShift.setEmployeeId(rs.getLong("employees_id"));
        employeeShift.setStartTime(rs.getTime("start_time").toLocalTime());
        employeeShift.setEndTime(rs.getTime("end_time").toLocalTime());

        return employeeShift;
    }
}
