package com.quasar.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.quasar.dao.AlbumRepository;
import com.quasar.model.Album;

@Controller
public class ProfileController
{
	@Autowired
	private AlbumRepository albumRepository;
	
	@RequestMapping(value = "/user-profile", method = RequestMethod.GET)
	public ModelAndView getUserProfilePage(@RequestParam Optional<String> error)
	{
		Map<String, Object> map = new HashMap<>();
		List<Album> albums = (List<Album>) albumRepository.findAll();
		
		map.put("albums", albums);
		return new ModelAndView("user_profile", map);
	}
}