package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.security.User;

public interface UserService {
    void createUser(User user);

    User getUser(String username);
}
