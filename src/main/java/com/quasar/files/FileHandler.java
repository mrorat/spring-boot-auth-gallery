package com.quasar.files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageProcessingException;
import com.quasar.model.Album;
import com.quasar.model.Image;
import com.quasar.service.ImageService;

@Service
public class FileHandler {
	
	private ImageService imageService;
	
    private ExecutorService executor = null;
    private ImageWriteParam iwp;

    @Autowired
    public FileHandler(ImageService imageService) {
    	this.imageService = imageService;
        this.executor = Executors.newFixedThreadPool(5);
    }

    private Image getImageOrThrow(String imageId) {
    	Optional<Image> image = imageService.getImageById(imageId);
    	if (!image.isPresent())
    		throw new RuntimeException("Image with ID: " + imageId + " does not exist in the database.");
    	return image.get();
    }
    
    public InputStreamWithSize getStreamWithSize(String albumId, String imageId) throws FileNotFoundException, IOException {
        File f = new File(getImageOrThrow(imageId).getPath());
        return new InputStreamWithSize(new FileInputStream(f), Files.size(f.toPath()));
    }

    public String getFileContentAsBase64(String albumId, String imageId) throws IOException {
        System.out.println(Instant.now() + " Request to get image id: " + imageId);
        String filePath = getImageOrThrow(imageId).getPath();
        File f = new File(filePath);
        InputStream finput = new FileInputStream(f);
        Throwable throwable = null;

        String base64image;
        try {
            byte[] imageBytes = new byte[(int)Files.size(f.toPath())];
            int bytesRead = finput.read(imageBytes, 0, imageBytes.length);
            if (bytesRead != imageBytes.length) {
                System.out.printf("ERROR: File [%s] was read and it's size [%d] did not match bytes read [%d]%n", f.getPath(), imageBytes.length, bytesRead);
            }

            base64image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Throwable ex) {
            throwable = ex;
            throw ex;
        } finally {
            if (finput != null) {
                if (throwable != null) {
                    try {
                        finput.close();
                    } catch (Throwable var17) {
                        throwable.addSuppressed(var17);
                    }
                } else {
                    finput.close();
                }
            }
        }

        return base64image;
    }

    public String getFileContentAsBase64Thumbnail(String albumId, String imageId) throws IOException {
    	long start = System.currentTimeMillis();
        System.out.println(Instant.now() + " Request to get thumbnail for image id: " + imageId);
        String filePath = getImageOrThrow(imageId).getThumbnailPath();
        File f = new File(filePath);
        InputStream finput = new FileInputStream(f);
        Throwable var6 = null;

        String var8;
        try {
            byte[] imageBytes = new byte[(int)Files.size(f.toPath())];
            finput.read(imageBytes, 0, imageBytes.length);
            var8 = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Throwable var17) {
            var6 = var17;
            throw var17;
        } finally {
            if (finput != null) {
                if (var6 != null) {
                    try {
                        finput.close();
                    } catch (Throwable var16) {
                        var6.addSuppressed(var16);
                    }
                } else {
                    finput.close();
                }
            }
        }

        System.out.println("Execution time [getFileContentAsBase64Thumbnail]: " + new Long(System.currentTimeMillis()-start).toString());
        return var8;
    }

    public void createThumbnail(File file) {
        Callable<Integer> task = () -> {
            String thumbnailFileName = this.getPathForThumbnailImage(file);
            File thumbnailFile = new File(thumbnailFileName);
            if (!thumbnailFile.exists() || thumbnailFile.length() == 0) {
                System.out.println(Instant.now() + "Creating thumbnail file for: " + file.getPath());
                FileImageOutputStream output = null;

                try {
                    File outputFile = new File(thumbnailFileName);
                    output = new FileImageOutputStream(outputFile);
                    this.writeToFile(file, output);
                } catch (Exception var8) {
                    var8.printStackTrace();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
            } else {
                System.out.println(Instant.now() + " Thumbnail file for: " + file.getPath() + " already exists");
            }

            return null;
        };
        this.executor.submit(task);
    }

    public BasicFileAttributes getFileAttributes(File file) throws IOException {
        return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    }

    private void writeToFile(File fileToWriteTo, FileImageOutputStream outputStream) throws IOException, ImageProcessingException {
        BufferedImage originalImage = ImageIO.read(fileToWriteTo);
//        Metadata metadata = ImageMetadataReader.readMetadata(fileToWriteTo);
        ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("jpeg").next();
        this.iwp = writer.getDefaultWriteParam();
        this.iwp.setCompressionMode(2);
        this.iwp.setCompressionQuality(0.25F);
        writer.setOutput(outputStream);
        IIOImage image = new IIOImage(originalImage, null, (IIOMetadata)null);
        writer.write(image.getMetadata(), image, this.iwp);
        System.out.println(Instant.now() + " Creating thumbnail file for: " + fileToWriteTo.getPath() + ", with size: " + fileToWriteTo.length());
    }
    
//    int THUMBNAIL_IMG_WIDTH = 800;
//    int THUMBNAIL_IMG_HEIGHT = 800;
//    private void resizeImageAndSave(File originalFile, FileImageOutputStream outputStream) throws IOException {
//    	BufferedImage originalImage = ImageIO.read(originalFile);
//    	BufferedImage resizedImage = null;
//    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
//    	
//    	int finalWidth = 0;
//    	int finalHeight = 0;
//    	if (Math.max(originalImage.getWidth(), originalImage.getHeight()) == originalImage.getWidth()) {
//    		float ratio = THUMBNAIL_IMG_WIDTH / originalImage.getWidth();
//    		resizedImage = new BufferedImage(THUMBNAIL_IMG_WIDTH, (int)(originalImage.getHeight() * ratio), type);
//    	} else {
//    		float ratio = THUMBNAIL_IMG_WIDTH / originalImage.getWidth();    		
//    		resizedImage = new BufferedImage((int)(originalImage.getWidth() * ratio), THUMBNAIL_IMG_WIDTH, type);
//    	}
//    	
//    	Graphics2D graphics = resizedImage.createGraphics();
//    	resizedImage.getGraphics().drawImage(originalImage.getScaledInstance(resizedImage.getWidth(), resizedImage.getHeight(), java.awt.Image.SCALE_SMOOTH), 0, 0, null);
//
//    	ImageIO.write(resizedImage, type, outputStream);
//    	outputStream.close();
//
//    	graphics.dispose();
//    }

    private String getPathForThumbnailImage(File file) {
        return file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()) + "thumbnail" + File.separator + file.getName();
    }

    public void createThumbnailDirectory(File directory) {
        File thumbnailDirectory = new File(directory.getPath() + File.separator + "thumbnail");
        if (!thumbnailDirectory.exists()) {
            System.out.printf("Creating thumbnail directory [%s]%n", thumbnailDirectory.getName());
            boolean result = thumbnailDirectory.mkdir();
            if (result) {
                System.out.printf("Thumbnail directory created [%s]%n", thumbnailDirectory.getPath());
            } else {
                System.out.printf("ERROR: Failed to create thumbnail directory created [%s]%n", thumbnailDirectory.getPath());
            }
        }
    }

    public void createUUIDFile(Album album) {
        File uuidFile = new File(album.getPath() + File.separator + "uuid");

        try {
            FileWriter fw = new FileWriter(uuidFile);
            Throwable throwable = null;

            try {
                fw.write(album.getAlbumid());
                fw.flush();
            } catch (Throwable ex) {
                throwable = ex;
                throw ex;
            } finally {
                if (fw != null) {
                    if (throwable != null) {
                        try {
                            fw.close();
                        } catch (Throwable ex) {
                            throwable.addSuppressed(ex);
                        }
                    } else {
                        fw.close();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}