package com.quasar.service;

import com.quasar.security.User;

public interface UserService {
    User save(User user);

    User getUserByEmail(String email);

    Iterable<User> getAllAdminUsers();

	User getUserById(String userId);
	
	boolean deleteUser(String userId);
}
