package com.quasar.controllers.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import com.quasar.security.User;

public class UserSafeDTO {

    private final String userid;
    private final String username;
    private final boolean enabled;
    private final Date createdDate;
    private final Date deletedDate;
    private final boolean isDeleted;
	private final Collection<? extends GrantedAuthority> roles;

    public UserSafeDTO(User user) {
    	this.userid = user.getID();
    	this.username = user.getUsername();
    	this.enabled = user.isEnabled();
    	this.createdDate = user.getCreatedDate();
    	this.deletedDate = user.getDeletedDate();
    	this.isDeleted = user.isDeleted();
    	this.roles = user.getAuthorities();
    }

	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public Collection<? extends GrantedAuthority> getRoles() {
		return roles;
	}
}