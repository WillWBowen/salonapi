package com.salon.www.salonapi.model;

import lombok.Data;

@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private String status;
}
