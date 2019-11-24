package com.quasar.service;

import com.quasar.model.Album;

import java.util.Optional;
import java.util.SortedSet;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface AlbumService {
    Album save(Album var1);

    @Cacheable({"albums"})
    Optional<Album> getAlbumById(String var1);

    SortedSet<Album> getAlbums();

    void renameAlbum(String var1, String var2);
    
    @Cacheable("userAlbums")
    SortedSet<Album> getAlbumsForUser(String userId);
    
    @CacheEvict("userAlbums")
    void evictCachedAlbums();
}
