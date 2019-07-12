package com.salon.www.salonapi.model;

import lombok.Data;

@Data
public class EmployeeShift {
    private Long id;
    private Long employeeId;
    private String day;
    private String startTime;
    private String endTime;
}
