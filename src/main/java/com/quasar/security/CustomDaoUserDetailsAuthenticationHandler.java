package com.quasar.security;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomDaoUserDetailsAuthenticationHandler extends DaoAuthenticationProvider {
	
    private final MyPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public CustomDaoUserDetailsAuthenticationHandler(MyPasswordEncoder passwordEncoder, UserDetailsService userDetailsService, LoginAttemptService loginAttemptService) {
    	this.passwordEncoder = passwordEncoder;
    	this.userDetailsService = userDetailsService;
    	this.loginAttemptService = loginAttemptService;
    }

    @PostConstruct
    public void postConstruct() {
        super.setUserDetailsService(this.userDetailsService);
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            
            // check if user is blocked
            if (loginAttemptService.isBlocked(userDetails.getUsername())) {
            	this.logger.warn("Authentication failed: user is blocked due to too many failed logins");
            	throw new BadCredentialsException("Authentication failed: user is blocked due to too many failed logins");
            }
            
            // password check
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(userDetails.getPassword(), presentedPassword)) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }
}