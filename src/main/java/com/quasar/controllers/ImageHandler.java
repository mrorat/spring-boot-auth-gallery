package com.quasar.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.quasar.files.FileHandler;
import com.quasar.files.InputStreamWithSize;

@RestController
public class ImageHandler
{
	
	@Autowired
	private FileHandler fileHandler;

	@RequestMapping(path="/images/{iid}", method=RequestMethod.GET)
	public void getImage(HttpServletResponse response, @PathVariable long iid) throws IOException
	{
		InputStreamWithSize myStreamWithSize = fileHandler.getStreamWithSize(iid);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=image.jpg");
		response.setContentType("image/jpeg");
		response.setContentLength((int)myStreamWithSize.getSize());

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStreamWithSize.getInputStream(), response.getOutputStream());
		response.flushBuffer();
	}
	
	@RequestMapping(path="/imagesbase64/{iid}", method=RequestMethod.GET)
	public void getImageAsBase64(HttpServletResponse response, @PathVariable long iid) throws IOException
	{
		String base64FileContent = fileHandler.getFileContentAsBase64(iid);
		
		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=image.jpg");
		response.setContentType("image/jpeg");
		response.setContentLength(base64FileContent.length());
		
		// Copy the stream to the response's output stream.
		response.getOutputStream().write(base64FileContent.getBytes());
		response.flushBuffer();
	}
}
