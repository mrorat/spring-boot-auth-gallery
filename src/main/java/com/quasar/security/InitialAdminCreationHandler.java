package com.quasar.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quasar.service.UserService;

@Component
public class InitialAdminCreationHandler {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InitialAdminCreationHandler.class);
	 
	@Autowired
	UserService userService;
	
	@PostConstruct
	public void postConstruct() {
		Set<User> adminUsers = userService.getAllAdminUsers();
		if (adminUsers.stream().filter(a -> 
			a.isAccountNonExpired() &&
			a.isAccountNonLocked() &&
			a.isCredentialsNonExpired() &&
			!a.isDeleted() &&
			a.isEnabled()).count() == 0) {
			
			// we do not have an admin for the application, lets create a new one and inform the user that started the application for
			// the first time what is the generated password
			String password = UUID.randomUUID().toString();
			
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			log.info("============================================ W A R N I N G ===============================================");
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			log.info("Creating an admin user as none were found in the database, assuming first startup of the service.");
			log.info("==========================================================================================================");
			log.info("Login credentials: user: admin, password: " + password);
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			Collection<Role> roles = new ArrayList<>();
			roles.add(new Role(ROLES.ROLE_ADMIN));
			User adminUser = new User("admin", password, roles);
			userService.save(adminUser);
		} else {
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			log.info("Admin user is already in the database");
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
			log.info("==========================================================================================================");
		}
	}
}
