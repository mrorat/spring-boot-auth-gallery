package com.quasar.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quasar.model.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, String> {
	Image save(Image var1);

	Image findOne(String id);
}