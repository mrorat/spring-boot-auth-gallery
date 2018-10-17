package com.quasar;

import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.quasar.service.DirectoryManagementService;

@SpringBootTest
public class DirectoryManagementServiceTest {

	@Mock
	private DirectoryManagementService directoryManagementService;
	
	@Test
	public void deleteEmptyDirectory() {
		File f = new File("some location");
		when(directoryManagementService.getGalleryDirectory()).thenReturn(f);
		File[] fileArray = new File[5];
		for (int i = 0; i < fileArray.length; i++) {
			fileArray[i] = new File("directory_" + i);
		}
		when(f.listFiles(directoryManagementService.getDirectoryFilter())).thenReturn(fileArray);
	}

}
