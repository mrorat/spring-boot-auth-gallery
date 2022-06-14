package com.quasar.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.quasar.Constants;
import com.quasar.GalleryApplication;
import com.quasar.controllers.dto.AlbumWithPermissions;
import com.quasar.controllers.dto.UserSafeDTO;
import com.quasar.dao.PermissionsRepository;
import com.quasar.dao.RoleRepository;
import com.quasar.dao.UserRepository;
import com.quasar.dto.AlbumNameChangeRequest;
import com.quasar.dto.PasswordDTO;
import com.quasar.dto.UserDTO;
import com.quasar.files.FileHandler;
import com.quasar.managers.AlbumManager;
import com.quasar.model.Album;
import com.quasar.model.AlbumPermission;
import com.quasar.model.Image;
import com.quasar.security.ROLES;
import com.quasar.security.Role;
import com.quasar.security.User;
import com.quasar.service.AlbumService;
import com.quasar.service.ImageService;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")

public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	private UserRepository userRepository;
	private AlbumManager albumManager;
	private AlbumService albumService;
	private PermissionsRepository permissionsRepository;
	private RoleRepository roleRepository;
    private FileHandler fileHandler;
    private ImageService imageService;
    
    @Value("${gallery.directory}")
    private String homeDirectory;

    @Autowired
	public AdminController(UserRepository userRepository, AlbumManager albumManager, PermissionsRepository permissionsRepository,
			RoleRepository roleRepository, AlbumService albumService, FileHandler fileHandler, ImageService imageService) {
		this.userRepository = userRepository;
		this.albumManager = albumManager;
		this.permissionsRepository = permissionsRepository;
		this.roleRepository = roleRepository;
		this.albumService = albumService;
		this.imageService = imageService;
		this.fileHandler = fileHandler;
	}

    @RequestMapping(
        value = {"/", ""},
        method = {RequestMethod.GET}
    )
    public ModelAndView getUserProfilePage(@RequestParam Optional<String> error) {
        Map<String, Object> map = new HashMap<>();
        map.put("albumCount", albumManager.getAllAlbums().size());
        map.put("userCount", userRepository.count());
        map.put("topNavActive", "home");
        
        return new ModelAndView("admin/administration", map);
    }
    
	/**
	 * User list page
	 * @param withDeleted - parameter used to specify if deleted users are returned
	 * @return ModelAndView with all users
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView getUserListPage(@RequestParam(value = "withDeleted", required=false, defaultValue = "false") boolean withDeleted) {
		Map<String, Object> map = new HashMap<>();
		List<User> users = userRepository.findAll();
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		{
			roles.add(ga.getAuthority());
		}
		map.put("roles", roles);
		map.put("topNavActive", "user-list");

		if (withDeleted)
			map.put("users", users);
		else
			map.put("users", users.stream().filter(u -> !u.isDeleted()).collect(Collectors.toList()));
		map.put("withDeleted", withDeleted);
		return new ModelAndView("admin/user_list", map);
	}
	
	
	@RequestMapping(value = "/show-roles", method = RequestMethod.GET)
	public ModelAndView getShowRolesPage()
	{
	    Map<String, Object> map = new HashMap<>();
        map.put("topNavActive", "show-roles");
		return new ModelAndView("admin/show_roles", map);
	}
	
	@GetMapping("/{userId}/albumPermissions")
	public ModelAndView getAlbumPermissionsPage(@PathVariable String userId, @RequestParam(value = "accessStatus", required=false, defaultValue = "all") String accessStatus) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			new ModelAndView("access_denied");
		}
		
		SortedSet<AlbumWithPermissions> albums = new TreeSet<>();
		Set<Album> allAlbums = albumManager.getAllAlbums();
		Set<Album> userAlbums = albumManager.getAlbumsForUser(userId);
		Set<Album> noAccessAlbums = allAlbums.stream()
		        .filter(a -> userAlbums.stream()
		                .noneMatch(ua -> ua.getAlbumid().equals(a.getAlbumid()))).collect(Collectors.toSet());
		Map<String, Object> map = new HashMap<>();

		switch (accessStatus) {
		    case "has_access":
		        albums.addAll(userAlbums.stream().map(x -> new AlbumWithPermissions(x, true)).collect(Collectors.toSet()));
		        map.put("accessType", "hasAccess");
                break;
		    case "no_access":
		        albums.addAll(noAccessAlbums.stream().map(x -> new AlbumWithPermissions(x, false)).collect(Collectors.toSet()));
		        map.put("accessType", "noAccess");
		        break;
            default :
                albums.addAll(noAccessAlbums.stream().map(x -> new AlbumWithPermissions(x, false)).collect(Collectors.toSet()));
                albums.addAll(userAlbums.stream().map(x -> new AlbumWithPermissions(x, true)).collect(Collectors.toSet()));
                map.put("accessType", "all");
                break;
        }
		
		map.put("albums", albums);
		map.put("userId", userId);
		map.put("userName", user.get().getUsername());
		
		return new ModelAndView("admin/album_permissions", map);
	}
	
	@GetMapping("/user/{userId}/passwordChange")
	public ModelAndView getPasswordChangeForm(@PathVariable String userId) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			return new ModelAndView("error/404");
		}
		Map<String, Object> map = new HashMap<>();
		
		map.put("userId", userId);
		map.put("userName", user.get().getUsername());
		map.put("password", new PasswordDTO());
		
		return new ModelAndView("admin/password_change", map);
	}
	
	@PostMapping(value="/user/{userId}/passwordChange", consumes={"application/x-www-form-urlencoded"})
    public RedirectView userPasswordChangeSubmit(@PathVariable String userId, @Valid @ModelAttribute("password") PasswordDTO passwordDTO, BindingResult result) {
        
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
        	user.get().setPassword(passwordDTO.getPassword());
        	
        	User savedUser = userRepository.save(user.get());
        	return new RedirectView("/admin/user/profile/" + savedUser.getID());
        } else return new RedirectView("error/404");        
    }
	
	@RequestMapping(path="/changeAlbumName/{albumId}", 
		method = RequestMethod.POST)
	public @ResponseBody Map<String, String> changeAlbumName(@PathVariable String albumId, @RequestBody AlbumNameChangeRequest body) {
		Map<String, String> map = new HashMap<>();
		
		Optional<Album> album = albumService.getAlbumById(albumId);
		if (album.isPresent()) {
			File albumFile = new File(album.get().getPath());
			LOGGER.info("Changing album: " + albumId + ", old name: " + albumFile.getName() +", new name: " + body.getAlbumName());
			
			if (!albumFile.exists()) {
				LOGGER.info("Directory ["+ albumFile.getAbsolutePath() + "] does not exists");
			}
			
			File newAlbumFile = new File(album.get().getPath().replace(albumFile.getName(), body.getAlbumName()));
			LOGGER.info("Old path: " + albumFile.getAbsolutePath());
			LOGGER.info("New path: " + newAlbumFile.getAbsolutePath());
			boolean renamingSuccess = albumFile.renameTo(newAlbumFile);

			if (renamingSuccess) {
				map.put("status", "renaming successful");
				album.get().rename(newAlbumFile);
				albumService.save(album.get());
				LOGGER.info("All good, ranaming successful.");
			}
			else {
				LOGGER.info("Renaming FAILED !!!");
				map.put("status", "renaming unsuccessful");
			}
		}
		else
			map.put("status", "album not found");
		
		return map;
	}
	
	@RequestMapping(path="/changeAlbumPermissions/{albumId}/{userId}/{isEnabled}", 
			method = RequestMethod.POST)
	public @ResponseBody Map<String, String> enableAlbumForUser(@PathVariable String albumId, @PathVariable String userId, @PathVariable boolean isEnabled, @RequestBody String body) {
		LOGGER.info("Changing permissions for user " + userId + ", album: " + albumId + " to " + (!isEnabled ? "ENABLED" : "DISABLED"));
		
		AlbumPermission albumPermission = permissionsRepository.findLastByAlbumAndUser(albumId, userId);
		
		Map<String, String> map = new HashMap<>();
		
		if (!isEnabled) { // we want to grant access to album
			if (albumPermission != null)
				map.put("result", "access already granted");
			else {
				permissionsRepository.save(new AlbumPermission.Builder()
						.setAlbumId(albumId)
						.setUserId(userId)
						.build());
				map.put("result", "access granted");
			}
		} else {  // we want to revoke access to album
			if (albumPermission != null) {
				permissionsRepository.save(new AlbumPermission.Builder()
						.setAlbumId(albumPermission.getId().getAlbumId())
						.setUserId(albumPermission.getId().getUserId())
						.setCreatedDate(albumPermission.getCreatedDate())
						.setDeletedDate(new Date())
						.build());
			
				map.put("result", "access revoked");
			} else {
				map.put("result", "access not granted");
			}			
		}
				
		return map;
	}
	
	@GetMapping(path = "/add-user")
	public ModelAndView addUser() {
		Map<String, Object> map = new HashMap<>();
		map.put("user", new UserDTO());
		List<String> roles = new ArrayList<>();
		for (ROLES role: ROLES.values()) {
			roles.add(role.name());
		}
		map.put("roles", roles);
        map.put("topNavActive", "add-user");
		return new ModelAndView("admin/new_user", map);
	}
	
	@PostMapping(value="/user", consumes={"application/x-www-form-urlencoded"})
    public RedirectView submit(@Valid @ModelAttribute("user") UserDTO userDTO, @ModelAttribute("role") String role, BindingResult result) {
        Role roleFromDb = roleRepository.findByName(role);
        
        User user = new User(userDTO.getName(), userDTO.getPassword(), Collections.singleton(roleFromDb));
        User savedUser = userRepository.save(user);
        
        return new RedirectView("/admin/user/" + savedUser.getID() + "/profile");
    }
	
	@GetMapping(value = "/user/{userid}/profile")
	public ModelAndView getSomeUserProfilePage(@RequestParam Optional<String> error, @PathVariable(name="userid") String userid)
	{
		Map<String, Object> map = new HashMap<>();
		User user = userRepository.findByUserid(userid);
		if (user != null) {
			Set<Album> albums = albumManager.getAlbumsForUser(userid);
			map.put("albums", albums);
			map.put("access_type", "admin");
			map.put("user", new UserSafeDTO(user));
			
		} else {
			return new ModelAndView("error/404");
		}
		
		return new ModelAndView("user/profile", map);
	}
	
	@DeleteMapping(value="/xxx/{userId}", consumes="application/json")
    public ModelAndView submit(@PathVariable String userId, @RequestBody String x) {
        
        User user = userRepository.findByUserid(userId);
        user.markAsDeleted();
        userRepository.save(user);

        return new ModelAndView("redirect:/admin/user");
    }
	
    @GetMapping("/album/refresh")
    public ModelAndView getRefreshAlbums(@RequestParam Optional<String> error) {
        Set<Album> allAlbums = albumManager.getAllAlbums();
        Set<String> albumNames = allAlbums.stream().map(a -> a.getName()).collect(Collectors.toSet());
        File[] albumFiles = getAlbumDirectories();
        Stream<File> albumStream = Arrays.stream(albumFiles);
        List<File> unknownAlbumDirectories = albumStream
                .parallel()
                .filter(f -> !albumNames.contains(Album.convertDirectoryNameToAlbumName(f.getName()))).sorted()
                .collect(Collectors.toList());
        
        List<File> albumsWithoutThumbnailsDirectory = Arrays.asList(albumFiles).stream()
                .filter(f -> !(new File(f.getAbsolutePath() + File.separator + Constants.THUMBNAILS_DIR).exists())).collect(Collectors.toList());
        List<File> albumsWithoutThumbnailFiles = Arrays.asList(albumFiles).stream()
                .filter(f -> !albumsWithoutThumbnailsDirectory.contains(f))
                .filter(
                        f -> f.listFiles(i -> i.getName().toLowerCase().endsWith(".jpg")).length 
                        > 
                        new File(f.getAbsolutePath() + File.separator + Constants.THUMBNAILS_DIR).listFiles(i -> i.getName().toLowerCase().endsWith(".jpg")).length)
                .collect(Collectors.toList());
        
        List<File> albumsToProcess = new ArrayList<>();
        albumsToProcess.addAll(unknownAlbumDirectories);
        albumsToProcess.addAll(albumsWithoutThumbnailsDirectory);
        albumsToProcess.addAll(albumsWithoutThumbnailFiles);
        
        processAlbums(albumsToProcess);

        return new ModelAndView("redirect:/gallery");
    }
    
    @GetMapping("/album/refresh/{albumId}")
    public ModelAndView refreshSpecificAlbum(@RequestParam Optional<String> error, @PathVariable("albumId") String albumId) {
        LOGGER.info("Refreshing specific album, with ID: " + albumId);
        Optional<Album> optionalAlbum = albumManager.getAlbumByIdForCurrentUser(albumId);
        
        if (optionalAlbum.isPresent()) {
            processAlbums(Collections.singletonList(new File(optionalAlbum.get().getPath())));
        } else {
        	User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	LOGGER.warn("Either album does not exist or user does not have access to it. Album ID: " + albumId + ", User ID: " + currentUser.getID());
        }
        
        return new ModelAndView("redirect:/gallery");
    }
    
    @Async
    private void processAlbums(List<File> albumsToProcess) {
        Map<String, String> postponedImageDuplicateUpdates = new HashMap<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        
        LOGGER.info("Processing " + albumsToProcess.size() + " albums");
        albumsToProcess.stream().forEach(albumFile -> executor.execute(new Runnable() {
            
            @Override
            public void run() {
                
                LOGGER.info("Thread [" + Thread.currentThread().getId() + "] processing album [" + albumFile.getName() + "]");
                File[] imageFileList = albumFile.listFiles((filex) -> {
                    return !filex.isDirectory() && filex.toString().toLowerCase().endsWith("jpg");
                });
                LOGGER.info("Thread [" + Thread.currentThread().getId() + "] found " + imageFileList.length + " images for album [" + albumFile.getName() + "]");
                Album album = createUUIDFileIfDoesntExistAndReturnAlbum(albumFile, new ArrayList<>());
                fileHandler.createThumbnailDirectory(albumFile);            
                Stream<File> imageFileStream = Arrays.stream(imageFileList);
                Set<String> imageNamesInAlbum = album.getImages().values().parallelStream().map(i -> i.getName()).collect(Collectors.toSet());
                
                albumService.save(album);
                
                List<File> unknownImagesInDirectory = imageFileStream
                        .filter(f -> !imageNamesInAlbum.contains(f.getName()))
                        .collect(Collectors.toList());
                LOGGER.info("Thread [" + Thread.currentThread().getId() + "] found " + imageFileList.length + " images we did not know before for album [" + albumFile.getName() + "]");

                List<Image> images = new ArrayList<>();
                for (File imageFile: unknownImagesInDirectory) {
                        Image image = processImageFile(album, imageFile, postponedImageDuplicateUpdates);
                        images.add(image);
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
                        fileHandler.createThumbnail(imageFile);
                        try {
                            imageService.save(image);
                        } catch (Exception ex) {
                            LOGGER.warn("Failed to save new image: " + image.toString());
                            postponedImageDuplicateUpdates.put(image.getId(), image.getDuplicateOfImageId());
                            image.setDuplicateOfImageId(null);
                            imageService.save(image);
                        }        
                }
                
            }
        }));
        
        while (executor.getActiveCount() != 0)
        {
            try {
                Thread.sleep(500);
                LOGGER.info("Active tasks: " + executor.getCompletedTaskCount() + "/" + albumsToProcess.size());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    @GetMapping("/album/full-refresh")
    public ModelAndView getFullRefreshAlbums(@RequestParam Optional<String> error) {
        File[] albumDirectories = getAlbumDirectories();
        if (albumDirectories != null) {

            Map<String, String> postponedImageDuplicateUpdates = new HashMap<>();
            for(int j = 0; j < albumDirectories.length; ++j) {
                File directory = albumDirectories[j];
                List<Image> images = new ArrayList<>();
                File[] imageFileList = directory.listFiles((filex) -> {
                    return !filex.isDirectory() && filex.toString().toLowerCase().endsWith("jpg");
                });
                Album album = this.createUUIDFileIfDoesntExistAndReturnAlbum(directory, images);
                this.fileHandler.createThumbnailDirectory(directory);
                
                if (imageFileList != null) {
                    LOGGER.info("Processing directory {}, image qty: {}", directory.getName(), imageFileList.length);

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
                        try {
                            imageService.save(image);
                        } catch (Exception ex) {
                            LOGGER.warn("Failed to save image: " + image.toString());
                            postponedImageDuplicateUpdates.put(image.getId(), image.getDuplicateOfImageId());
                            image.setDuplicateOfImageId(null);
                            imageService.save(image);
                        }
                    }
                }

                if (images.size() > 1) {
                    ((Image)images.get(0)).setPreviousId(((Image)images.get(images.size() - 1)).getId());
                    ((Image)images.get(images.size() - 1)).setNextId(((Image)images.get(0)).getId());
                }

                album.setImages(images);
                LOGGER.info("Album {} [ID: {}] created with {} images", album.getName(), album.getAlbumid(), images.size());
            }
            
            for (String imageId : postponedImageDuplicateUpdates.keySet()) {
                Optional<Image> i = imageService.getImageById(imageId);
                if (i.isPresent()) {
                    i.get().setDuplicateOfImageId(postponedImageDuplicateUpdates.get(imageId));
                    imageService.save(i.get());
                }
            }
        }

        return new ModelAndView("redirect:/gallery");
    }
    
    private Image processImageFile(Album album, File imageFile, Map<String, String> postponedImageDuplicateUpdates) {
        long start = System.currentTimeMillis();
        Image image = new Image(imageFile, album.getAlbumid().toString(), this.fileHandler);
        long imageCreated = System.currentTimeMillis() - start;
        /* at this point we should know if this image already exist in our database
         * since we generate image id from the file CRC */
        
        Optional<Image> imageFromDbOptional = imageService.getImageById(image.getId());
        long optionalImageFetchedFromDb = System.currentTimeMillis() - start - imageCreated;
        if (imageFromDbOptional.isPresent() && !imageFromDbOptional.get().getPath().equals(image.getPath())) {
            image = imageFromDbOptional.get().createRefToDuplicate(image);
        }

        long beforeReturn = System.currentTimeMillis() - start - imageCreated - optionalImageFetchedFromDb;
        LOGGER.info("Image created: [{}], imageFetched: [{}], beforeReturn: [{}]", imageCreated, optionalImageFetchedFromDb, beforeReturn);
        return image;
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

    private File[] getAlbumDirectories() {
        File galleryHomeDirectory = new File(GalleryApplication.getGalleryHomeDirectory() == null ? homeDirectory : GalleryApplication.getGalleryHomeDirectory());
        return galleryHomeDirectory.listFiles((filex) -> {
            return filex.isDirectory();
        });
    }
}
