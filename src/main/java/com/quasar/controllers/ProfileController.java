package com.quasar.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController
{
	@RequestMapping(value = "/user-profile", method = RequestMethod.GET)
	public String getUserProfilePage(@RequestParam Optional<String> error)
	{
		return "user_profile";
	}
}