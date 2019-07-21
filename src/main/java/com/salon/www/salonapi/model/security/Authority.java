package com.salon.www.salonapi.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    private Long id;
    private AuthorityName name;
    private List<User> users;

    public Authority(AuthorityName name) {
        this.name = name;
    }

}
