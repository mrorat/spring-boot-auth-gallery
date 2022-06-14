package com.quasar.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.quasar.security.User;

public interface UserRepository extends CrudRepository<User, String> {
	
    @SuppressWarnings("unchecked")
	User save(User user);

    User findByUserid(String user);

    @Query(
        value = "SELECT * FROM user u JOIN users_roles ur ON ur.userid=u.userid JOIN role r ON r.roleid=ur.roleid WHERE r.name=?1",
        nativeQuery = true
    )
    Set<User> findAllByRoleName(String roleName);

    @Query(
            value = "SELECT * FROM user u WHERE u.username=?1 AND u.enabled=1 AND u.deleted_date IS NULL",
            nativeQuery = true
        )
	User findByUsername(String userName);
    
    List<User> findAll();
}
