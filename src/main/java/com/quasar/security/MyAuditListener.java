package com.quasar.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyAuditListener {

    private static final Logger LOG = LoggerFactory.getLogger("security");
    
    @Autowired
    LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuditEvent(AuditApplicationEvent event) {
        AuditEvent auditEvent = event.getAuditEvent();
        LOG.info("type={}, principal={}", auditEvent.getType(), auditEvent.getPrincipal());
        
        switch (auditEvent.getType()) {
			case "AUTHENTICATION_SUCCESS":
				loginAttemptService.loginSucceeded(auditEvent.getPrincipal());
				break;
			case "AUTHENTICATION_FAILURE":
				LOG.warn("type={}, principal={}, data={}", auditEvent.getType(), auditEvent.getPrincipal(), auditEvent.getData());
				break;
			case "AUTHORIZATION_FAILURE":
				LOG.warn("type={}, principal={}, data={}", auditEvent.getType(), auditEvent.getPrincipal(), auditEvent.getData());
				break;
		}
    }
}