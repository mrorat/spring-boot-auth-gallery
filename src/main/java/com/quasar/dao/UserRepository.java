package com.quasar.dao;

import com.quasar.security.User;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	
    User save(User var1);

    User findByUserid(String var1);

    @Query(
        value = "SELECT * FROM user u JOIN users_roles ur ON ur.userid=u.userid JOIN role r ON r.roleid=ur.roleid WHERE r.name=?1",
        nativeQuery = true
    )
    Iterable<User> findAllByRoleName(String var1);

    User findByUsername(String var1);
    
    List<User> findAll();
}
