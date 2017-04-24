package com.quasar.controllers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.quasar.model.Image;

@Controller
public class GalleryController
{

	@RequestMapping(value = "/album", method = RequestMethod.GET)
	public ModelAndView getAllAlbums(@RequestParam Optional<String> error)
	{
		Map<String, Object> map = new HashMap<>();
		SortedSet<String> albums = new TreeSet<String>();
		File f = new File("c:\\code\\gallery\\img\\");
		for (File directory : f.listFiles(new FileFilter() {
												@Override
												public boolean accept(File pathname)
												{
													return (pathname.isDirectory());
												}
											}
										))
		{
			albums.add(directory.getName());
		}
		map.put("albums", albums);
		return new ModelAndView("gallery", map);
	}
	
	@RequestMapping(path = "/album/{albumName}", method = RequestMethod.GET)
	public ModelAndView getUserProfilePage(@PathVariable String albumName, @RequestParam Optional<String> error)
	{
		Map<String, Object> map = new HashMap<>();
		List<Image> images = new ArrayList<Image>();
		File f = new File("c:\\code\\gallery\\img\\album_1");
		File[] files = f.listFiles(
			new FileFilter() {
				@Override
				public boolean accept(File pathname)
				{
					return (!pathname.isDirectory() && pathname.toString().toLowerCase().endsWith("jpg"));
				}
			}
		);
		for (File file : files)
		{
			images.add(new Image(file.getName()));
		}
		map.put("images", images);
		return new ModelAndView("album", map);
	}
}