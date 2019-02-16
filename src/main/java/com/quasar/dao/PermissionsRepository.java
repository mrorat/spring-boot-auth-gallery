package com.quasar.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quasar.model.AlbumPermission;

@Repository
public interface PermissionsRepository extends CrudRepository<AlbumPermission, String> {
	
	@SuppressWarnings("unchecked")
	AlbumPermission save(AlbumPermission var1);

    @Query(
            value = "SELECT * FROM album_permissions ap WHERE ap.albumid=:albumId AND ap.userid=:userId AND (ap.deleted_date IS NULL OR ap.deleted_date>NOW()) ORDER BY ap.created_date DESC LIMIT 1",
            nativeQuery = true
        )
	AlbumPermission findLastByAlbumAndUser(@Param("albumId") String albumId, @Param("userId") String userId);	
}