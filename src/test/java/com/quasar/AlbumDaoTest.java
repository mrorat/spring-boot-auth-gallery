package com.quasar;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.quasar.dao.UserRepository;
import com.quasar.security.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlbumDaoTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testStoredProcedure() {
    	
    }
    
    @Test
    public void testFindByname() {
    	User user = userRepository.findByUsername("michal");
  
    	Assert.assertNotNull(user);
    	Assert.assertEquals("b08a9fbe-6c98-4f12-9efb-aca97aa81673", user.getID());
    }
    
    @Test
    public void testSavingUser() {
    	User user = new User();
    	user.setEnabled(true);
    	user.setPassword("someHardToGuessPassword");
    	String userName = UUID.randomUUID().toString();
		user.setUsername(userName);
    	User savedUser = userRepository.save(user);
    	
    	Assert.assertEquals(userName, savedUser.getUsername());
    	
    	User fetchedUser = userRepository.findByUserid(savedUser.getID());
    	Assert.assertEquals(userName, fetchedUser.getUsername());
    }
    
    @Test
    public void testIfUniqueUsernameConstraintWorks() {
    	
    	String userName = UUID.randomUUID().toString();
    	
    	User user = new User();
    	user.setEnabled(true);
    	user.setPassword("someHardToGuessPassword");
    	user.setUsername(userName);
    	User savedUser = userRepository.save(user);
    	
    	Assert.assertNotNull(savedUser);

    	User user2 = new User();
    	user2.setEnabled(true);
    	user2.setPassword("someHardToGuessPassword");
    	user2.setUsername(userName);
    	User savedUser2 = userRepository.save(user2);

    	Assert.assertNotNull(savedUser2);
    }
    
    @Test()
    public void testIfUniqueUsernameConstraintTriggersIfUserIsDeleted() {
    	
    	String userName = UUID.randomUUID().toString();
    	
    	User user = new User();
    	user.setEnabled(true);
    	user.setPassword("someHardToGuessPassword");
    	user.setUsername(userName);
    	User savedUser1a = userRepository.save(user);
    	
    	savedUser1a.markAsDeleted();
    	User savedUser1b = userRepository.save(savedUser1a);
    	Assert.assertTrue(savedUser1b.isDeleted());
    	
    	User user2 = new User();
    	user2.setEnabled(true);
    	user2.setPassword("someHardToGuessPassword");
    	user2.setUsername(userName);
    	User successfullySavedUser2 = userRepository.save(user2);
    	Assert.assertNotNull(successfullySavedUser2);
    	Assert.assertEquals(userName, successfullySavedUser2.getUsername());
    }

    @After
    public void tearDown() {
//        db.shutdown();
    }

}