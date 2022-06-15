package com.quasar.managers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.quasar.dao.PermissionsRepository;
import com.quasar.dao.UserRepository;
import com.quasar.dto.CustomAlbumDTO;
import com.quasar.model.Album;
import com.quasar.model.AlbumPermission;
import com.quasar.security.ROLES;
import com.quasar.security.User;
import com.quasar.service.AlbumService;
import com.quasar.service.UserService;

@Service
public class AlbumManager {

    @Autowired
    private AlbumService albumService;   
    @Autowired
    private UserService userService;   
    @Autowired
    private UserRepository userRepository;   
    @Autowired
    private PermissionsRepository permissionsRepository;
    
	public Set<Album> getAlbumsForCurrentUser() {
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (currentUser.hasRole(ROLES.ROLE_ADMIN))
			return this.albumService.getAlbums();  // add customisation there to hide / mark favourite etc... albums
		return this.albumService.getAlbumsForUser(currentUser.getID());
	}

	public Set<Album> getAlbumsForUser(String userId) {
		User user = userService.getUserById(userId);
		if (user.hasRole(ROLES.ROLE_ADMIN))
			return this.albumService.getAlbums();  // add customisation there to hide / mark favourite etc... albums
		return this.albumService.getAlbumsForUser(user.getID());
	}

	public Set<Album> getAllAlbums() {
		return this.albumService.getAlbums();
	}
	
	public Optional<Album> fingById(String id) {
	    return this.albumService.getAlbumById(id);
	}

    public Optional<Album> getAlbumByIdForCurrentUser(String albumId) {
        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return albumService.getAlbumsForUser(currentUser.getID()).stream().filter(a -> a.getAlbumid().equalsIgnoreCase(albumId)).findFirst();
    }

	public void saveNewCustomAlbum(String ownerUsername, @Valid CustomAlbumDTO customAlbumDTO) {
		Album albumToPersist = new Album(customAlbumDTO.getName());
		Album savedAlbum = this.albumService.save(albumToPersist);
		User userSavingCustomAlbum = userRepository.findByUsername(ownerUsername);
		permissionsRepository.save(new AlbumPermission.Builder()
				.setAlbumId(savedAlbum.getAlbumid())
				.setUserId(userSavingCustomAlbum.getID())
				.build());
	}
	
	public List<Album> getCustomAlbumsForCurrentUser() {
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.albumService.getCustomAlbumsForUser(currentUser.getID());
	}
}