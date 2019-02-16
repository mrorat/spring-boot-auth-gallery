package com.quasar.controllers.advice;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.quasar.service.UserService;

@ControllerAdvice(annotations = Controller.class)
public class AnnotationAdvice {

	@Autowired
	UserService userService;

	@ModelAttribute("currentUser")
	public UIUser getCurrentUser() {
		if (SecurityContextHolder.getContext().getAuthentication() != null)
		{
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userDetails != null)
				return new UIUser(userDetails);
		}
		return null;
	}
	
	public class UIUser {
		private String username;
		private Collection<? extends GrantedAuthority> roles;

		UIUser(UserDetails userDetails) {
			this.username = userDetails.getUsername();
			this.roles = userDetails.getAuthorities();
		}

		public String getUsername() {
			return username;
		}

		public Collection<? extends GrantedAuthority> getRoles() {
			return roles;
		}
		
		
	}
}