package com.quasar.service;

import com.quasar.dao.UserRepository;
import com.quasar.security.Roles;
import com.quasar.security.User;
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

    public Iterable<User> getAllAdminUsers() {
        return this.userDAO.findAllByRoleName(Roles.ROLE_ADMIN.name());
    }
}
