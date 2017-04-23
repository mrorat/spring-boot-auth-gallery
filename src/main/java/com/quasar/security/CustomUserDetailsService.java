package com.quasar.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService
{
	public String aa = "AAAAAAAAAAAAAAAAAAAAAAA";

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		try
		{
			User user = getUser(username); // userRepository.findByUsername(username);
			if (user == null)
			{
				LOGGER.debug("user not found with the provided username");
				return null;
			}
			LOGGER.debug(" user from username " + user.toString());
			return new org.springframework.security.core.userdetails.User(user.getUsername(), "root123", //user.getPassword(),
					getAuthorities(user));
		} catch (Exception e)
		{
			throw new UsernameNotFoundException("User not found");
		}
	}

	private User getUser(String username)
	{
		User u = new User();
		switch (username)
		{
			case "admin":
				u.setUsername(username);
				u.setRoles("ROLE_ADMIN", "ROLE_USER");
				return u;
			case "user":
				u.setUsername(username);
				u.setRoles("ROLE_USER");
				return u;
			default:
				return null;
		}
	}

	private Set<GrantedAuthority> getAuthorities(User user)
	{
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (Role role : user.getRoles())
		{
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
			authorities.add(grantedAuthority);
			LOGGER.debug(String.format("User: %s, role: %s", user.getUsername(), role.getRole()));
		}
		return authorities;
	}

}