package com.quasar.service;

import java.io.File;
import java.util.Iterator;
import java.util.Optional;
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

    public Optional<Album> getAlbumById(String albumId) {
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
        Optional<Album> album = this.getAlbumById(id);
        if (album.isPresent()) {
            File oldNameAlbum = new File(album.get().getPath());
            File newNameAlbum = new File(album.get().getPath() + "_renamed");
            oldNameAlbum.renameTo(newNameAlbum);
            if (newNameAlbum.exists()) {
                album.get().rename(newNameAlbum);
            }
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
	
	public void evictCachedAlbums(){}
}
