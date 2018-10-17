package com.quasar.dao;

import com.quasar.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, String> {
    Album save(Album var1);

    Album findByAlbumid(String var1);
}
