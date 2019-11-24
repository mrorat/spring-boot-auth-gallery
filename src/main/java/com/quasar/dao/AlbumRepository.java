package com.quasar.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quasar.model.Album;

@Repository
public interface AlbumRepository extends CrudRepository<Album, String> {
    @SuppressWarnings("unchecked")
	Album save(Album var1);

    @Query(
            value = "SELECT * FROM album a JOIN album_permissions ap ON ap.albumid=a.albumid WHERE a.albumid=?1 AND ap.userid=?2 AND ap.created_date<NOW() AND (ap.deleted_date IS NULL OR ap.deleted_date>NOW())",
            nativeQuery = true)
    Optional<Album> findByAlbumidForUser(String albumId, String userId);
    
    @Query(
            value = "SELECT * FROM album a JOIN album_permissions ap ON ap.albumid=a.albumid WHERE ap.userid=?1 AND ap.created_date<NOW() AND (ap.deleted_date IS NULL OR ap.deleted_date>NOW())",
            nativeQuery = true)
	List<Album> getAlbumsForUser(String userid);

	Optional<Album> findByAlbumid(String albumId);
}
