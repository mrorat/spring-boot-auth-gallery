package com.quasar.managers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.quasar.model.Album;
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
}