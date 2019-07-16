package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrator {
    private long id;
    private long userId;
    private String firstName;
    private String lastName;

    public Administrator(long userId, String firstName, String lastName) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
