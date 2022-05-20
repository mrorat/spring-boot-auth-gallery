package com.quasar.controllers;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quasar.controllers.dto.Rotation;
import com.quasar.files.FileHandler;
import com.quasar.files.InputStreamWithSize;
import com.quasar.model.Image;
import com.quasar.security.User;
import com.quasar.service.AlbumService;
import com.quasar.service.ImageService;

@RestController
public class ImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHandler.class);
    @Value(value="thumbnails.cache.max.age")
    private static final int cacheMaxAge = 7776000;
	private FileHandler fileHandler;
    private AlbumService albumService;
    private ImageService imageService;

    @Autowired
    public ImageHandler(FileHandler fileHandler, AlbumService albumService, ImageService imageService) {
    	this.fileHandler = fileHandler;
    	this.albumService = albumService;
    	this.imageService = imageService;
    }

    @RequestMapping(
        path = {"/images/{iid}"},
        method = {RequestMethod.GET}
    )
    public void getImage(HttpServletResponse response, @PathVariable String albumId, @PathVariable String imageId) throws IOException {
        InputStreamWithSize myStreamWithSize = this.fileHandler.getStreamWithSize(albumId, imageId);
        Throwable var5 = null;

        try {
            this.modifyResponseHeaders(response, (int)myStreamWithSize.getSize(), imageId, 7776000);
            IOUtils.copy(myStreamWithSize.getInputStream(), response.getOutputStream());
            myStreamWithSize.getInputStream().close();
            response.flushBuffer();
        } catch (Throwable var14) {
            var5 = var14;
            throw var14;
        } finally {
            if (myStreamWithSize != null) {
                if (var5 != null) {
                    try {
                        myStreamWithSize.close();
                    } catch (Throwable var13) {
                        var5.addSuppressed(var13);
                    }
                } else {
                    myStreamWithSize.close();
                }
            }

        }

    }

    @RequestMapping(
        path = {"/imagesbase64/thumbnails/{albumId}/{imageId}"},
        method = {RequestMethod.GET}
    )
    public void getThumbnailImageAsBase64(HttpServletResponse response, @PathVariable String albumId, @PathVariable String imageId) throws IOException {
    	String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    	LOGGER.info(userName);
        String base64FileContent = this.fileHandler.getFileContentAsBase64Thumbnail(albumId, imageId);
        this.modifyResponseHeaders(response, base64FileContent.length(), imageId, cacheMaxAge);
        response.getOutputStream().write(base64FileContent.getBytes());
        response.flushBuffer();
    }

    @RequestMapping(
        path = {"/imagesbase64/{albumId}/{imageId}"},
        method = {RequestMethod.GET}
    )
    public void getImageAsBase64(HttpServletResponse response, @PathVariable String albumId, @PathVariable String imageId) throws IOException {
        String base64FileContent = this.fileHandler.getFileContentAsBase64(albumId, imageId);
        this.modifyResponseHeaders(response, base64FileContent.length(), imageId, 7776000);
        response.getOutputStream().write(base64FileContent.getBytes());
        response.flushBuffer();
    }

    @RequestMapping(
        path = {"/getImageDescription/{albumId}/{imageId}"},
        method = {RequestMethod.GET}
    )
    public void getImageDescription(HttpServletResponse response, @PathVariable String albumId, @PathVariable String imageId) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Optional<Image> image = imageService.getImageById(imageId);
        if (!image.isPresent()) {
        	LOGGER.info("Image not found in the database, image ID: " + imageId);
        	return;
        }
        response.setContentType("application/json");
        response.getOutputStream().write(gson.toJson(image).getBytes());
        response.flushBuffer();
    }

    @RequestMapping(
        path = {"/selectImageAsAlbumBanner/{albumId}/{imageId}"},
        method = {RequestMethod.GET}
    )
    public void selectImageAsAlbumBanner(@PathVariable String imageId, @PathVariable String albumId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.info("User {} selected image {} as a banner for album {}%n", userName, imageId, albumId);
    }

    @RequestMapping(
        path = {"/renameAlbum/{albumId}/{newAlbumName}"},
        method = {RequestMethod.GET}
    )
    public void renameAlbum(@PathVariable String albumId, @PathVariable String newAlbumName) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        this.albumService.renameAlbum(albumId, newAlbumName);
        LOGGER.info("User [%s] selected renamed album [ID: %s] to new name [%s]%n", userName, albumId, newAlbumName);
    }
    
    @RequestMapping(
            path = {"/images/{iid}/rotate"},
            method = {RequestMethod.POST}
        )
    public void rotateImage(@PathVariable String iid, @RequestParam("rotation") Rotation rotation) {
    	String userId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getID();
    	String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    	this.imageService.saveRotation(iid, rotation, userId);
    	LOGGER.info("User [%s] rotated image [ID: %s] by [%s]%n", userName, iid, rotation);
    }

    private void modifyResponseHeaders(HttpServletResponse response, int contentLength, String imageId, int cacheMaxAge) {
        response.setContentLength(contentLength);
        response.setContentType("image/jpeg");
        response.addHeader("content-disposition", "attachment;filename=" + imageId + ".jpg");
        response.addHeader("cache-control", "private, max-age=" + cacheMaxAge);
        response.addHeader("expires", Instant.now().plusSeconds(7776000L).toString());
    }
}
