package com.quasar.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.quasar.Constants;

@Controller
public class HelloWorldController {

	
	@GetMapping({ "/", "/home" })
	public ModelAndView homePage(ModelMap model) {
		return new ModelAndView(Constants.REDIRECT_GALLERY);
	}


//	@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
//	public String dbaPage(ModelMap model) {
//		model.addAttribute("user", getPrincipal());
//		return "userprofile";
//	}

	@GetMapping("/logout")
	   public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      if (auth != null){    
	         new SecurityContextLogoutHandler().logout(request, response, auth);
	      }
	      return "welcome";
	   }
	
	@GetMapping("/welcome")
	public ModelAndView welcomePage() {
	    return new ModelAndView("welcome");
	}

	@GetMapping("/Access_Denied")
	public String accessDeniedPage(ModelMap model) {
//		model.addAttribute("user", getPrincipal());
		return "accessDenied";
	}

}