//package com.quasar;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//
//import com.quasar.database.generated.proxy.DatabaseProxy;
//
//public class AlbumDaoTest {
//
//    private DatabaseProxy dbProxy;
//    
//    @Test
//    public void testStoredProcedure() {
//    	
//    }
//    
//    @Test
//    public void testFindByname() {
//    	NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
//    	UserDaoImpl userDao = new UserDaoImpl();
//    	userDao.setNamedParameterJdbcTemplate(template);
//    	
//    	User user = userDao.findByName("mkyong");
//  
//    	Assert.assertNotNull(user);
//    	Assert.assertEquals(1, user.getId().intValue());
//    	Assert.assertEquals("mkyong", user.getName());
//    	Assert.assertEquals("mkyong@gmail.com", user.getEmail());
//
//    }
//
//    @After
//    public void tearDown() {
////        db.shutdown();
//    }
//
//}