package com.salon.www.salonapi.model.security;

import com.salon.www.salonapi.model.NewUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    private Date lastPasswordResetDate;
    private List<Authority> authorities;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(NewUser newUser) {
            this.username = newUser.getUsername();
            this.password =newUser.getPassword();
            this.email = newUser.getEmail();
    }

}
