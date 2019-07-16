package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String password;
    private String token;

    public UserDto(String username, String password){
        this.username = username;
        this.password = password;
    }

    public UserDto(Long id, String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
    }
}
