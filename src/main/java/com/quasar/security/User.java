package com.quasar.security;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

	public User()
	{
		roles.add(new Role("ROLE_GUEST"));
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(String... roles)
	{
		for (String role : roles)
		{
			this.roles.add(new Role(role));
		}
	}
}