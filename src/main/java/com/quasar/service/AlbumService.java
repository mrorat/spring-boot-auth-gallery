package com.quasar.service;

import com.quasar.model.Album;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface AlbumService {
    Album save(Album var1);

    @Cacheable({"albums"})
    Optional<Album> getAlbumById(String albumId);

    SortedSet<Album> getAlbums();

    void renameAlbum(String albumId, String newName);
    
    @Cacheable("userAlbums")
    SortedSet<Album> getAlbumsForUser(String userId);
    
    @CacheEvict("userAlbums")
    void evictCachedAlbums();

	List<Album> getCustomAlbumsForUser(String userId);
}
