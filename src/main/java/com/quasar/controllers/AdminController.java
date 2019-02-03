package com.quasar.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.quasar.dao.UserRepository;
import com.quasar.security.User;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

	private UserRepository userRepository;
	
	@Autowired
	public AdminController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    @RequestMapping(
        value = {"/admin"},
        method = {RequestMethod.GET}
    )
    public String getUserProfilePage(@RequestParam Optional<String> error) {
        return "admin/administration";
    }
    
	
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public ModelAndView getUserListPage()
	{
		Map<String, Object> map = new HashMap<>();
		List<User> users = userRepository.findAll();
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		{
			roles.add(ga.getAuthority());
		}
		map.put("roles", roles);
		map.put("users", users);
		return new ModelAndView("admin/user_list", map);
	}
	
	
	@RequestMapping(value = "/admin/show-roles", method = RequestMethod.GET)
	public ModelAndView getShowRolesPage()
	{
		Map<String, Object> map = new HashMap<>();
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		{
			roles.add(ga.getAuthority());
		}
		map.put("roles", roles);
		return new ModelAndView("admin/show_roles", map);
	}
}
