package com.quasar.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quasar.controllers.dto.Rotation;
import com.quasar.dao.ImageRepository;
import com.quasar.model.Image;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
	
	@Autowired
	private ImageRepository imageDAO;

	public ImageServiceImpl() {
	}

	public Image save(Image image) {
		return this.imageDAO.save(image);
	}

	public Optional<Image> getImageById(String id) {
		return this.imageDAO.findById(id);
	}

	public SortedSet<Image> getImages() {
		SortedSet<Image> images = new TreeSet<>();
		Iterator<Image> var2 = this.imageDAO.findAll().iterator();

		while (var2.hasNext()) {
			Image image = (Image) var2.next();
			images.add(image);
		}

		return images;
	}
	

	@Override
	public SortedSet<Image> getImagesForUser(String albumId) {
		List<Image> images = imageDAO.getImagesForAlbum(albumId);
//		markBrokenImages(images);
		return new TreeSet<Image>(images);
	}

//	private void markBrokenImages(List<Image> images) {
//		images.stream().filter(i -> !i.fileExists()).forEach(i -> i.set);
//	}

	@Override
	public void saveRotation(String iid, Rotation rotation, String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markImageAsNonExistent(String imageId) {
		Optional<Image> image = imageDAO.findById(imageId);
		if (image.isPresent()) {
			image.get().markAsNonExistentFile();
			imageDAO.save(image.get());
		}
	}

}