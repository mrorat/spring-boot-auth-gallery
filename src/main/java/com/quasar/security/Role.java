package com.quasar.security;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String role;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<User> users;

	public Role(String role)
	{
		this.role = role;
	}

	public String getRole()
	{
		return role;
	}
}
