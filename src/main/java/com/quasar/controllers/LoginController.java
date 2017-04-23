package com.quasar.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController
{

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(@RequestParam Optional<String> error)
	{
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		{
			System.out.println(ga.getAuthority());
		}

		return "login_form";
	}

	@RequestMapping(value = "/access-denied", method = RequestMethod.GET)
	public String getAccessDeniedPage(@RequestParam Optional<String> error)
	{
		return "access_denied";
	}
	
	@RequestMapping(value = "/show-roles", method = RequestMethod.GET)
	public ModelAndView getShowRolesPage()
	{
		Map<String, Object> map = new HashMap<>();
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		{
			roles.add(ga.getAuthority());
		}
		map.put("roles", roles);
		return new ModelAndView("show_roles", map);
	}
}