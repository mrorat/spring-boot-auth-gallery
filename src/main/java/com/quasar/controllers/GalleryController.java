package com.quasar.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.quasar.GalleryApplication;
import com.quasar.files.FileHandler;
import com.quasar.managers.AlbumManager;
import com.quasar.model.Album;
import com.quasar.model.Image;
import com.quasar.repository.Repository;
import com.quasar.service.AlbumService;
import com.quasar.service.ImageService;

@Controller
public class GalleryController {
	
    private static SortedSet<Album> albums = new TreeSet<>();
    
    @Autowired
    private AlbumService albumService;    
    
    @Autowired
    private AlbumManager albumManager;
    
    @Autowired
    private FileHandler fileHandler;
    
    
    String homeDirectory = "c:\\__FOTO\\__TEST";

    @Autowired
	private ImageService imageService;

    @Autowired
    public GalleryController() {
    }

    @GetMapping("/album/refresh")
    public ModelAndView getRefreshAlbums(@RequestParam Optional<String> error) {
        albums.clear();
        File galleryHomeDirectory = new File(GalleryApplication.getGalleryHomeDirectory() == null ? homeDirectory : GalleryApplication.getGalleryHomeDirectory());
        File[] albumFileList = galleryHomeDirectory.listFiles((filex) -> {
            return filex.isDirectory();
        });
        if (albumFileList != null) {

            for(int j = 0; j < albumFileList.length; ++j) {
                File directory = albumFileList[j];
                List<Image> images = new ArrayList<>();
                File[] imageFileList = directory.listFiles((filex) -> {
                    return !filex.isDirectory() && filex.toString().toLowerCase().endsWith("jpg");
                });
                Album album = this.createUUIDFileIfDoesntExistAndReturnAlbum(directory, images);
                this.fileHandler.createThumbnailDirectory(directory);
                if (imageFileList != null) {
                    System.out.printf("Processing directory %s, image qty: %d%n", directory.getName(), imageFileList.length);

                    albumService.save(album);
                    for(int i = 0; i < imageFileList.length; ++i) {
                        File file = imageFileList[i];
                        Image image = new Image(file, album.getAlbumid().toString(), this.fileHandler);
                        
                        /* at this point we should know if this image already exist in our database
                         * since we generate image id from the file CRC */
                        
                        Optional<Image> imageFromDbOptional = imageService.getImageById(image.getId());
                        if (imageFromDbOptional.isPresent() && !imageFromDbOptional.get().getPath().equals(image.getPath())) {
                        	image = imageFromDbOptional.get().createRefToDuplicate(image);
                        }
                        
                        images.add(image);
                        this.fileHandler.createThumbnail(file);
                        if (images.size() > 1) {
                            Image previousImage = (Image)images.get(images.size() - 2);
                            previousImage.setNextId(image.getId());
                            Optional<Image> prevImageFromDbOptional = imageService.getImageById(previousImage.getId());
                            if (prevImageFromDbOptional.isPresent()) {
                            	Image prevImageFromDb = prevImageFromDbOptional.get();
                            	prevImageFromDb.setNextId(image.getId());
                            	imageService.save(prevImageFromDb);
                            	image.setPreviousId(previousImage.getId());
                            }
                        }
                        imageService.save(image);
                    }
                }

                if (images.size() > 1) {
                    ((Image)images.get(0)).setPreviousId(((Image)images.get(images.size() - 1)).getId());
                    ((Image)images.get(images.size() - 1)).setNextId(((Image)images.get(0)).getId());
                }

                album.setImages(images);
                System.out.printf("Album %s [ID: %s] created with %d images%n", album.getName(), album.getAlbumid(), images.size());
                albums.add(album);
            }
        }

        Repository.setAlbums(albums);
        return new ModelAndView("redirect:/gallery");
    }

    private Album createUUIDFileIfDoesntExistAndReturnAlbum(File directory, List<Image> images) {
        File uuidFile = new File(directory + File.separator + "uuid");
        Album album = new Album(directory, images);
        if (uuidFile.exists()) {
            try {
                album.setId((String)Files.readAllLines(uuidFile.toPath()).get(0));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return album;
        } else {
            this.fileHandler.createUUIDFile(album);
            return this.albumService.save(album);
        }
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
        return new ModelAndView("gallery", map);
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
//        return new ModelAndView("album", map);
        return new ModelAndView("gallery_cat_grid", map);
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
