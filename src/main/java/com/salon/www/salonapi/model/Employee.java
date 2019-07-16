package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String position;
    private String status;

    public Employee(long userId, String firstName, String lastName) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Employee(long id, long userId, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
