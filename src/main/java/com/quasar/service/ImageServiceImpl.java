package com.quasar.service;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public Image getImageById(String id) {
		return this.imageDAO.findOne(id);
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
}