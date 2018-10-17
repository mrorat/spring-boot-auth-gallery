package com.quasar.service;

import com.quasar.security.Role;

public interface RoleService {
    Role save(Role var1);

    Role getRoleByRoleid(String var1);

    Role getRoleByName(String var1);
}
