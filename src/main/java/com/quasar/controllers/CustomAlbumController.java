package com.quasar.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.quasar.dto.CustomAlbumDTO;
import com.quasar.managers.AlbumManager;
import com.quasar.model.Album;

@Controller
@RequestMapping("/custom-album")
public class CustomAlbumController {
	
    @Autowired
    private AlbumManager albumManager;
    
	@GetMapping(value="/new")
	public ModelAndView createCustomAlbumPage() {
		Map<String, Object> map = new HashMap<>();
		map.put("album", new CustomAlbumDTO());
		return new ModelAndView("custom-album/new_form", map);
	}

	@GetMapping(value="/list")
	public ModelAndView listCustomAlbums() {
		Map<String, Object> map = new HashMap<>();
		List<Album> customAlbumsForCurrentUser = this.albumManager.getCustomAlbumsForCurrentUser();
		map.put("albums", customAlbumsForCurrentUser);
		return new ModelAndView("custom-album/new_form", map);
	}

	@PostMapping(value="/save", consumes={"application/x-www-form-urlencoded"})
	public RedirectView persistNewCustomAlbum(@Valid @ModelAttribute("album") CustomAlbumDTO customAlbumDTO, BindingResult result) {
        
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		albumManager.saveNewCustomAlbum(userDetails.getUsername(), customAlbumDTO);
        
        return new RedirectView("/custom-album/list");
    }
}