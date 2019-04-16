package com.quasar.info;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import com.quasar.dao.UserRepository;

@Component
public class ActuatorCustomInfo implements InfoContributor {
	 
    @Autowired
    UserRepository userRepository;
 
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("users", getUserDetails());
    }
    
    private Map<String, Object> getUserDetails() {
    	Map<String, Object> userDetails = new HashMap<>();
    	userDetails.put("counter.all", (int)userRepository.count());
    	return userDetails;    	
    }
}