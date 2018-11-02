package com.quasar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.quasar.model.Album;
import com.quasar.dao.*;;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest()
public class GalleryApplicationTests {

	@Autowired
	private AlbumRepository albumRepository;
 
	@Test
	public void contextLoads() {
//		List<Album> albums = repository.findById("jakiesId");
//		assertTrue(albums.size() > 0);
		for (Album album : albumRepository.findAll()) {
			System.out.println(album.getName());
		}
	}

}
