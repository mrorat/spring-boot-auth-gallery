package com.quasar.service;

import java.util.Set;

import com.quasar.security.User;

public interface UserService {
    User save(User user);

    User getUserByEmail(String email);

    Set<User> getAllAdminUsers();

	User getUserById(String userId);
	
	boolean deleteUser(String userId);
}
