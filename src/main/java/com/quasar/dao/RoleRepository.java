package com.quasar.dao;

import com.quasar.security.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
    Role save(Role var1);

    Role findByRoleid(String var1);

    Role findByName(String var1);
}