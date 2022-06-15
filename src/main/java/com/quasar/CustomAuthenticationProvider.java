package com.quasar;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	
	public CustomAuthenticationProvider()
	{
		LOGGER.info("*** CustomAuthenticationProvider created");
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{

		if (authentication.getName().equals("admin") && authentication.getCredentials().equals("admin"))
		{
			List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(),
					grantedAuths);
		} else
		{
			return null;
		}

	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}