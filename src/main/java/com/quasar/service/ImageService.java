package com.quasar.service;

import java.util.SortedSet;

import org.springframework.cache.annotation.Cacheable;

import com.quasar.controllers.dto.Rotation;
import com.quasar.model.Image;

public interface ImageService {
    Image save(Image var1);

    @Cacheable({"images"})
    Image getImageById(String id);

    SortedSet<Image> getImages();
    
    SortedSet<Image> getImagesForUser(String userid);

	void saveRotation(String iid, Rotation rotation, String userId);
}
