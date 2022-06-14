package com.quasar.service;

import java.util.Optional;
import java.util.SortedSet;

import org.springframework.cache.annotation.Cacheable;

import com.quasar.controllers.dto.Rotation;
import com.quasar.model.Image;

public interface ImageService {
    Image save(Image var1);

    @Cacheable({"images"})
    Optional<Image> getImageById(String id);

    SortedSet<Image> getImages();
    
    SortedSet<Image> getImagesForUser(String albumId);

	void saveRotation(String iid, Rotation rotation, String userId);

	void markImageAsNonExistent(String imageId);
}
