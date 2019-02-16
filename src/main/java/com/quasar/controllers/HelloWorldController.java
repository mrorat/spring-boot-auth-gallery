package com.quasar.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloWorldController {

	
	@GetMapping({ "/", "/home" })
	public String homePage(ModelMap model) {
		return "welcome";
	}


//	@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
//	public String dbaPage(ModelMap model) {
//		model.addAttribute("user", getPrincipal());
//		return "userprofile";
//	}

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	   public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      if (auth != null){    
	         new SecurityContextLogoutHandler().logout(request, response, auth);
	      }
	      return "welcome";
	   }

	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
//		model.addAttribute("user", getPrincipal());
		return "accessDenied";
	}

}