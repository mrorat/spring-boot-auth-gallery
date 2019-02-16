//package com.quasar.service;
//
//import java.io.File;
//import java.io.FileFilter;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DirectoryManagementService {
//
//	private File galleryHome;
//	
//	@Value(value="${gallery.directory}")
//	private String galleryDirectory;
//	
//	public DirectoryManagementService(@Value(value="${gallery.directory}") String galleryDirectory)
//	{
//		this.galleryHome = new File(galleryDirectory);
//		if (!galleryHome.exists() || !galleryHome.isDirectory())
//			throw new RuntimeException("Home directory does not exist or is not a directory [" + galleryDirectory + "]");
//	}
//	
//	public boolean deleteEmptyDirectory(String directoryToDeletePath) {
//		while (directoryToDeletePath.startsWith(File.pathSeparator) || directoryToDeletePath.startsWith("..")) {
//			directoryToDeletePath = directoryToDeletePath.substring(directoryToDeletePath.startsWith(File.pathSeparator) ? 1 : 2);
//		}
//		if (directoryToDeletePath.length() == 0)
//			throw new RuntimeException("Unable to delete gallery home directory");
//		
//		File directoryToDelete = new File(directoryToDeletePath);
//		if (!directoryToDelete.exists() || !directoryToDelete.isDirectory() || directoryToDelete.listFiles().length > 0)
//			throw new RuntimeException("Directory does not exist or is not a directory or is not empty, [" + directoryToDeletePath+ "]");
//
//		return directoryToDelete.delete();
//	}
//
//	public File getGalleryDirectory() {
//		galleryHome = new File(galleryDirectory);
//		return galleryHome;
//	}
//
//	FileFilter directoryFilter = new FileFilter() {
//		@Override
//		public boolean accept(File pathname)
//		{
//			return (pathname.isDirectory());
//		}
//	};
//	
//	public FileFilter getDirectoryFilter() {
//		return directoryFilter;
//	}	
//}