package com.quasar.security;

import com.quasar.dao.UserRepository;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    @Autowired
    UserRepository userRepository;

    public CustomUserDetailsService() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.userRepository.findByUsername(username);
            if (user == null) {
                LOGGER.debug("user not found with the provided username");
                throw new UsernameNotFoundException("User not found");
            } else {
                LOGGER.debug(" user from username " + user.toString());
                return user;
            }
        } catch (Exception var3) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private User getUser(String username) {
        User u = new User();
        byte var4 = -1;
        switch(username.hashCode()) {
        case -1074367764:
            if (username.equals("michal")) {
                var4 = 0;
            }
            break;
        case 100506:
            if (username.equals("ela")) {
                var4 = 1;
            }
            break;
        case 3343912:
            if (username.equals("mama")) {
                var4 = 2;
            }
        }

        switch(var4) {
        case 0:
            u.setUsername(username);
            u.setRoles(new Roles[]{Roles.ROLE_ADMIN, Roles.ROLE_USER});
            return u;
        case 1:
        case 2:
            u.setUsername(username);
            u.setRoles(new Roles[]{Roles.ROLE_USER});
            return u;
        default:
            return null;
        }
    }
}
