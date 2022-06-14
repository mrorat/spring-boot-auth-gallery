package com.quasar.service;

import com.quasar.dao.UserRepository;
import com.quasar.security.ROLES;
import com.quasar.security.User;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userDAO;

    public UserServiceImpl() {
    }

    public User save(User user) {
        return this.userDAO.save(user);
    }

    public User getUserByEmail(String userID) {
        return this.userDAO.findByUserid(userID);
    }

    public Set<User> getAllAdminUsers() {
        return this.userDAO.findAllByRoleName(ROLES.ROLE_ADMIN.name());
    }

	@Override
	public User getUserById(String userId) {
		return this.userDAO.findByUserid(userId);
	}

    @Override
    public boolean deleteUser(String userId) {
        User userToDelete = getUserById(userId);
        if (userToDelete == null || userToDelete.isDeleted())
            return false;
        userToDelete.markAsDeleted();
        userDAO.save(userToDelete);
        return true;
    }
}