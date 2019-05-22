package com.quasar.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.quasar.managers.AlbumManager;
import com.quasar.model.Album;
import com.quasar.model.Image;
import com.quasar.service.AlbumService;
import com.quasar.service.ImageService;

@Controller
public class GalleryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GalleryController.class);
	
    private static SortedSet<Album> albums = new TreeSet<>();
    
    @Autowired
    private AlbumService albumService;    
    
    @Autowired
    private AlbumManager albumManager;
    
    String homeDirectory = "c:\\__FOTO\\__TEST";

    @Autowired
	private ImageService imageService;

    @Autowired
    public GalleryController() {
    }

    @GetMapping("/save")
    public ModelAndView saveAlbum() {
        this.albumService.save(new Album(new File("C:/temp/1"), new ArrayList<>()));
        Map<String, Object> map = new HashMap<>();
        return new ModelAndView("gallery", map);
    }

    @GetMapping("/gallery")
    public ModelAndView getAllAlbums(@RequestParam Optional<String> error) {
//        if (albums.isEmpty()) {
//            albums = this.albumService.getAlbumsForUser(SecurityContextHolder.getContext().getAuthentication().getName());
//        }

        System.out.printf("Loaded %d albums from database", albums.size());
        Map<String, Object> map = new HashMap<>();
        map.put("albums", this.albumManager.getAlbumsForCurrentUser());
        return new ModelAndView("album_list", map);
    }

    @GetMapping("/gallery2")
    public ModelAndView getGallery2(@RequestParam Optional<String> error) {
        if (albums.isEmpty()) {
            albums = this.albumService.getAlbums();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("albums", albums);
        return new ModelAndView("gallery2", map);
    }

    @GetMapping("/album2")
    public ModelAndView getAllAlbums2(@RequestParam Optional<String> error) {
        if (albums.isEmpty()) {
            albums = this.albumService.getAlbums();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("albums", albums);
        return new ModelAndView("gallery2", map);
    }

    @RequestMapping(
        path = {"/album/{albumName}/{albumId}"},
        method = {RequestMethod.GET}
    )
    public ModelAndView getImagesForAlbum(@PathVariable String albumName, @PathVariable String albumId, @RequestParam Optional<String> error) {
        Map<String, Object> map = new HashMap<>();
        Album album = albumService.getAlbumById(albumId);
        Set<Image> imagesForAlbum = imageService.getImagesForUser(albumId);
        System.out.printf("get images for album: [%s] %s, images %d%n", album.getAlbumid(), album.getName(), imagesForAlbum.size());
        map.put("images", imagesForAlbum);
        map.put("albumName", album.getName());
        map.put("albumId", album.getAlbumid());
        return new ModelAndView("album_grid", map);
    }
    
    @RequestMapping(
    		path = {"/albumOrder/{albumName}/{albumId}"},
    		method = {RequestMethod.GET}
    		)
    public ModelAndView getOrderedImagesForAlbum(@PathVariable String albumName, @PathVariable String albumId, @RequestParam Optional<String> error) {
    	Map<String, Object> map = new HashMap<>();
    	Album album = albumService.getAlbumById(albumId);
    	Set<Image> imagesForAlbum = imageService.getImagesForUser(albumId);
    	System.out.printf("get images for album: [%s] %s, images %d%n", album.getAlbumid(), album.getName(), imagesForAlbum.size());
    	map.put("images", imagesForAlbum);
    	map.put("albumName", album.getName());
    	map.put("albumId", album.getAlbumid());
    	return new ModelAndView("gallery_order", map);
    }

    @GetMapping("/picture/{albumId}/{imageId}")
    public ModelAndView showImage(@PathVariable String albumId, @PathVariable String imageId, @RequestParam Optional<String> error) {
        Map<String, Object> map = new HashMap<>();
        System.out.println("get images for albumId: " + albumId + ", image: " + imageId);
        map.put("albumName", albumService.getAlbumById(albumId).getName());
        map.put("albumId", albumId);
        map.put("imageId", imageId);
        return new ModelAndView("picture", map);
    }
}