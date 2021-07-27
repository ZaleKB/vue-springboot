package com.zale.service;

import com.zale.entity.User;

public interface UserService {
    void register(User user);

    User login(User user);
}
