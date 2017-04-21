package com.quasar.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class FileHandler
{
	public InputStreamWithSize getStreamWithSize(long iid) throws FileNotFoundException, IOException
	{
		File f = new File("img/IMG_5612_thumbnail.jpg");
		return new InputStreamWithSize(new FileInputStream(f), Files.size(f.toPath()));
	}
	
	public String getFileContentAsBase64(long iid) throws IOException
	{
		File f = new File("img/IMG_5612_thumbnail.jpg");
		InputStream finput = new FileInputStream(f);
		byte[] imageBytes = new byte[(int)Files.size(f.toPath())];
		finput.read(imageBytes, 0, imageBytes.length);
		finput.close();
		return Base64.getEncoder().encodeToString(imageBytes);
	}
}