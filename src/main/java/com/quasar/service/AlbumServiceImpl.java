package com.quasar.service;

import com.quasar.dao.AlbumRepository;
import com.quasar.model.Album;
import java.io.File;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumRepository albumDAO;

    public AlbumServiceImpl() {
    }

    public Album save(Album album) {
        return this.albumDAO.save(album);
    }

    public Album getAlbumById(String id) {
        return this.albumDAO.findByAlbumid(id);
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
}
