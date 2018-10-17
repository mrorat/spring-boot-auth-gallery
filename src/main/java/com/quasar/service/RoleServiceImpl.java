package com.quasar.service;

import com.quasar.dao.RoleRepository;
import com.quasar.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleDAO;

    public RoleServiceImpl() {
    }

    public Role save(Role role) {
        return this.roleDAO.save(role);
    }

    public Role getRoleByRoleid(String roleId) {
        return this.roleDAO.findByRoleid(roleId);
    }

    public Role getRoleByName(String name) {
        return this.roleDAO.findByName(name);
    }
}
