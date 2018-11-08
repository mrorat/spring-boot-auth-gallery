package com.quasar.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quasar.model.Album;

@Repository
public interface AlbumRepository extends CrudRepository<Album, String> {
    @SuppressWarnings("unchecked")
	Album save(Album var1);

    Album findByAlbumid(String var1);
    
    @Query(
            value = "SELECT * FROM album a JOIN album_permissions ap ON ap.albumid=a.albumid WHERE ap.deleted_date IS NULL AND ap.userid=?1",
            nativeQuery = true
        )
	List<Album> getAlbumsForUser(String userid);
}
