package com.quasar.service;

import java.io.File;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quasar.dao.AlbumRepository;
import com.quasar.model.Album;
import com.quasar.security.ROLES;
import com.quasar.security.User;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumRepository albumDAO;
    @Autowired
    private UserService userService;

    public AlbumServiceImpl() {
    }

    public Album save(Album album) {
        return this.albumDAO.save(album);
    }

    public Album getAlbumById(String albumId) {
    	User user = userService.getUserById(getCurrentUserId());
    	if (user.hasRole(ROLES.ROLE_ADMIN))
    		return this.albumDAO.findByAlbumid(albumId);
    	else
    		return this.albumDAO.findByAlbumidForUser(albumId, user.getID());
    }

    public SortedSet<Album> getAlbums() {
        SortedSet<Album> albums = new TreeSet<>();
        Iterator<Album> var2 = this.albumDAO.findAll().iterator();

        while(var2.hasNext()) {
            Album album = (Album)var2.next();
            albums.add(album);
        }

        return albums;
    }

    public void renameAlbum(String id, String newName) {
        Album album = this.getAlbumById(id);
        File oldNameAlbum = new File(album.getPath());
        File newNameAlbum = new File(album.getPath() + "_renamed");
        oldNameAlbum.renameTo(newNameAlbum);
        if (newNameAlbum.exists()) {
            album.rename(newNameAlbum);
        }

    }

	@Override
	public SortedSet<Album> getAlbumsForUser(String userid) {
		// TODO Auto-generated method stub
		return new TreeSet<>(albumDAO.getAlbumsForUser(userid));
	}

	private String getCurrentUserId() {
		return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getID();
	}
}
