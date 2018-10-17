//package com.quasar;
//
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.wix.mysql.config.MysqldConfig;
//import com.quasar.model.Album;
//import com.quasar.repository.GalleryRepository;
//import com.wix.mysql.EmbeddedMysql;
//import com.wix.mysql.ScriptResolver;
//
//import java.util.concurrent.TimeUnit;
//
//import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
//import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
//import static com.wix.mysql.distribution.Version.v5_6_23;
//import static com.wix.mysql.config.Charset.UTF8;
//
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class EmbeddedDatabaseTest {
//
//	EmbeddedMysql mysqld;
//	
//	@Autowired
//	private GalleryRepository galleryRepository;
//	
//	@Before
//	public void before() {
//
//		MysqldConfig config = aMysqldConfig(v5_6_23)
//		    .withCharset(UTF8)
//		    .withPort(2215)
//		    .withUser("gallery", "gallery")
//		    .withTimeout(5, TimeUnit.SECONDS)
//		    .withServerVariable("max_connect_errors", 666)
//		    .build();
//		
////		InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/index.html");
////		InputStream is = this.getClass().getClassLoader().getResourceAsStream("db/001_init.sql");
////		System.out.print(is.toString());
//
//		mysqld = anEmbeddedMysql(config)
//		    .addSchema("gallery", ScriptResolver.classPathScript("db/001_init.sql"))
//		    .start();
//
//	}
//
//	@Test
//	public void test() {
//		Album a = galleryRepository.findById("test");
//		System.out.println(a.toString());
//		fail("Not yet implemented");
//	}
//
//	@After
//	public void after() {
//
//		mysqld.stop(); //optional, as there is a shutdown hook
//	}
//}