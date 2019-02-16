package com.quasar.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quasar.model.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, String> {
	@SuppressWarnings("unchecked")
	Image save(Image var1);

//	Image findOne(String id);

    @Query(
            value = "SELECT * FROM image i JOIN album a ON a.albumid=i.albumid WHERE a.albumid=?1",
            nativeQuery = true
        )
	List<Image> getImagesForUser(String userid);
}