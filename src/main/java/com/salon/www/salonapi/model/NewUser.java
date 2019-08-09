package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String phone;

}
