package com.quasar.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.quasar.controllers.dto.UserSafeDTO;
import com.quasar.dao.AlbumRepository;
import com.quasar.dao.UserRepository;
import com.quasar.dto.PasswordDTO;
import com.quasar.model.Album;
import com.quasar.security.User;

@Controller
@RequestMapping("/user")
public class ProfileController
{
	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value = "/profile")
	public ModelAndView getUserProfilePage(@RequestParam Optional<String> error)
	{
		Map<String, Object> map = new HashMap<>();
		
		List<Album> albums = (List<Album>) albumRepository.findAll();
		map.put("albums", albums);

		if (SecurityContextHolder.getContext().getAuthentication() != null)
		{
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userDetails != null) {
				User currentUser = userRepository.findByUsername(userDetails.getUsername());
				map.put("user", new UserSafeDTO(currentUser));
			}
		}
		return new ModelAndView("user/profile", map);
	}
	
	@GetMapping(path = "/passwordChangeForm")
	public ModelAndView passwordChange() {
		Map<String, Object> map = new HashMap<>();
		map.put("password", new PasswordDTO());
		
		return new ModelAndView("user/password_change", map);
	}
	
	@PostMapping(value="/changePassword", consumes={"application/x-www-form-urlencoded"})
	public RedirectView submitPasswordChange(@Valid @ModelAttribute("password") PasswordDTO passwordDTO, BindingResult result) {
        
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        User user = userRepository.findByUsername(userDetails.getUsername());
        user.setPassword(passwordDTO.getPassword());
        User savedUser = userRepository.save(user);
        
        Map<String, Object> model = new HashMap<>();
        model.put("user", new UserSafeDTO(savedUser));
        return new RedirectView("/user/profile/" + savedUser.getID());
    }

}