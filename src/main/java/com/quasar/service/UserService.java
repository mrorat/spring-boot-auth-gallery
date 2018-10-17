package com.quasar.service;

import com.quasar.security.User;

public interface UserService {
    User save(User var1);

    User getUserByEmail(String var1);

    Iterable<User> getAllAdminUsers();
}
