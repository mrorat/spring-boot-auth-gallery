package com.quasar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.quasar.dao.AlbumRepository;
import com.quasar.dao.PermissionsRepository;
import com.quasar.dao.UserRepository;
import com.quasar.model.Album;
import com.quasar.model.AlbumPermission;
import com.quasar.security.User;;

@RunWith(SpringRunner.class)
@DataJpaTest
//@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class GalleryApplicationTests {

	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private PermissionsRepository permissionsRepository;
	
	@Autowired
	private UserRepository userRepository;
 
	@Test
	public void contextLoads() {
//		List<Album> albums = repository.findById("jakiesId");
//		assertTrue(albums.size() > 0);
		for (Album album : albumRepository.findAll()) {
			System.out.println(album.getName());
		}
		
	}
	
	@Test
	public void userWithoutAlbumPermissionsTest() {
		String userId = "c8ddbd3e-4992-404d-a718-non-existent";
		String albumId = "48e46702-7152-41bc-a580-non-existent";
		AlbumPermission albumPermission = permissionsRepository.findLastByAlbumAndUser(albumId, userId);
		
		assertNull(albumPermission);		
	}
	
	@Test
	public void userWithAlbumPermissionsDeletedTest() {

		User user = createNewUser();
		Album album = createNewAlbum();
		AlbumPermission savedAlbumPermission = permissionsRepository.save(new AlbumPermission
				.Builder()
				.setUserId(user.getID())
				.setAlbumId(album.getAlbumid())
				.setCreatedDate(new Date()).build());
		assertNotNull(savedAlbumPermission);		

		AlbumPermission albumPermission = permissionsRepository.findLastByAlbumAndUser(album.getAlbumid(), user.getID());
		
		assertNotNull(albumPermission);		
		
		AlbumPermission deletedAlbumPermission = new AlbumPermission.Builder()
				.setAlbumId(albumPermission.getId().getAlbumId())
				.setUserId(albumPermission.getId().getUserId())
				.setCreatedDate(albumPermission.getCreatedDate())
				.setDeletedDate(new Date())
				.build();
		permissionsRepository.save(deletedAlbumPermission);
		AlbumPermission albumPermissionAfterDeletion = permissionsRepository.findLastByAlbumAndUser(album.getAlbumid(), user.getID());
		
		assertNull(albumPermissionAfterDeletion);
	}
	
	private User createNewUser() {
		User newUser = new User();
		newUser.setUsername(UUID.randomUUID().toString());
		newUser.setPassword(UUID.randomUUID().toString());
		return userRepository.save(newUser);
	}
	
	private Album createNewAlbum() {
		Album newAlbum = new Album(new File("some directory"), Collections.emptyList());
		newAlbum.setId(UUID.randomUUID().toString());
		return albumRepository.save(newAlbum);
	}

}
