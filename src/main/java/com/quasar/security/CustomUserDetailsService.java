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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.userRepository.findByUsername(username);
            if (user == null) {
                LOGGER.debug("user not found with the provided username");
                throw new UsernameNotFoundException("User not found");
            } else {
                LOGGER.debug(" user from username {}", user.toString());
                return user;
            }
        } catch (Exception var3) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}