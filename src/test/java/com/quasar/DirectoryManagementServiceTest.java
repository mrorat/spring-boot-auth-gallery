//package com.quasar;
//
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.io.FileFilter;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.quasar.service.DirectoryManagementService;
//
//import ch.qos.logback.core.rolling.helper.FileFilterUtil;
//
//@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
//public class DirectoryManagementServiceTest {
//
//	@Mock
//	private DirectoryManagementService directoryManagementService;
//	
//	@Mock
//	private File file;
//	
//	@Test
//	public void deleteEmptyDirectory() {
//		File f = new File("some location");
//		when(directoryManagementService.getGalleryDirectory()).thenReturn(f);
//		File[] fileArray = new File[5];
//		for (int i = 0; i < fileArray.length; i++) {
//			fileArray[i] = new File("directory_" + i);
//		}
//		when(directoryManagementService.getDirectoryFilter()).thenReturn(new FileFilter() {
//			
//			@Override
//			public boolean accept(File pathname) {
//				// TODO Auto-generated method stub
//				return pathname.isDirectory();
//			}
//		});
//		
//		when(file.listFiles(directoryManagementService.getDirectoryFilter())).thenReturn(fileArray);
//	}
//
//}
