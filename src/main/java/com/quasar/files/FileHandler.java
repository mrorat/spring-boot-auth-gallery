package com.quasar.files;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quasar.Constants;
import com.quasar.model.Album;
import com.quasar.model.Image;
import com.quasar.service.ImageService;

import io.micrometer.core.annotation.Timed;
import mediautil.image.jpeg.AbstractImageInfo;
import mediautil.image.jpeg.Entry;
import mediautil.image.jpeg.Exif;
import mediautil.image.jpeg.LLJTran;

@Service
public class FileHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);
	private ImageService imageService;
	
    private ExecutorService executor = null;
//    private ImageWriteParam iwp;

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
        LOGGER.info("Request to get image id: " + imageId);
        String filePath = getImageOrThrow(imageId).getPath();
        File f = new File(filePath);
        InputStream finput = new FileInputStream(f);
        Throwable throwable = null;

        String base64image;
        try {
            byte[] imageBytes = new byte[(int)Files.size(f.toPath())];
            int bytesRead = finput.read(imageBytes, 0, imageBytes.length);
            if (bytesRead != imageBytes.length) {
                LOGGER.info("ERROR: File [{}] was read and it's size [{}] did not match bytes read [{}]%n", f.getPath(), imageBytes.length, bytesRead);
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

    @Timed
    public String getFileContentAsBase64Thumbnail(String albumId, String imageId) throws IOException {
    	long start = System.currentTimeMillis();
        LOGGER.info("Request to get thumbnail for image id: " + imageId);
        String filePath = getImageOrThrow(imageId).getThumbnailPath();
        File f = new File(filePath);
        InputStream finput = new FileInputStream(f);
        Throwable var6 = null;

        String thumbnailAsBase64;
        try {
            byte[] imageBytes = new byte[(int)Files.size(f.toPath())];
            finput.read(imageBytes, 0, imageBytes.length);
            thumbnailAsBase64 = Base64.getEncoder().encodeToString(imageBytes);
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

        LOGGER.info("Execution time [getFileContentAsBase64Thumbnail]: " + new Long(System.currentTimeMillis()-start).toString());
        return thumbnailAsBase64;
    }

    public void createThumbnail(File file) {
        Callable<Integer> task = () -> {
            String thumbnailFilePath = this.getPathForThumbnailImage(file);
            File thumbnailFile = new File(thumbnailFilePath);
            if (!thumbnailFile.exists() || thumbnailFile.length() == 0) {
                long thumbnailSize = resizeImage(ImageIO.read(file), thumbnailFilePath);
                System.out.printf("Resized file from %d to %d - file: %s%n", thumbnailSize, file.length(), thumbnailFile.getAbsolutePath());

                rotateThumbnailIfNecessary(thumbnailFilePath, file);
                
            } else {
                LOGGER.debug("Thumbnail file for: " + file.getPath() + " already exists");
            }

            return null;
        };
        this.executor.submit(task);
    }

    private final int maxDimension = 400; // 400px
    
    long resizeImage(BufferedImage originalImage, String outputImagePath) throws IOException {
//        int targetWidth = originalImage.getWidth() < 400 ? originalImage.getWidth() : 400;
//        int targetHeight = (int) (originalImage.getHeight() * (400.0f / (float)originalImage.getWidth()));
        Dimension thumbnailDimension = getThumbnailDimention(originalImage);
        java.awt.Image resultingImage = originalImage.getScaledInstance(thumbnailDimension.width, thumbnailDimension.height, java.awt.Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(thumbnailDimension.width, thumbnailDimension.height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        File outputThumnailFile = new File(outputImagePath);
        ImageIO.write(outputImage, "jpg", outputThumnailFile);
        return outputThumnailFile.getTotalSpace();
    }
    
    private Dimension getThumbnailDimention(BufferedImage originalImage) {
        if (originalImage.getWidth() > originalImage.getHeight()) {
            return new Dimension(maxDimension, (int) (originalImage.getHeight() * ((float)maxDimension / (float)originalImage.getWidth())));
        } else {
            return new Dimension((int) (originalImage.getWidth() * ((float)maxDimension / (float)originalImage.getHeight())), maxDimension);
        }
    }
    
    void rotateThumbnailIfNecessary(String thumbnailFileName, File originalImage) {
        try {
            // Read image EXIF data
//            File imageFile = new File(originalImagePath);
            LLJTran llj = new LLJTran(originalImage);
            llj.read(LLJTran.READ_INFO, true);
            AbstractImageInfo<?> imageInfo = llj.getImageInfo();
            if (!(imageInfo instanceof Exif)) {
                System.out.println("Image has no EXIF data");
                throw new Exception("Image has no EXIF data");
            }

            int operation = 0;
            // Determine the orientation
            Exif exif = (Exif) imageInfo;
            int orientation = 1;
            Entry orientationTag = exif.getTagValue(Exif.ORIENTATION, true);
            if (orientationTag != null)
                orientation = (Integer) orientationTag.getValue(0);
            
            // Determine required transform operation
            if (orientation > 0
                    && orientation < Exif.opToCorrectOrientation.length)
                operation = Exif.opToCorrectOrientation[orientation];
            
            if (operation == 0)
            {
                System.out.println("Rotation not necessary for file: " + thumbnailFileName);
                return;
            }
            File thumbnailImageFile = new File(thumbnailFileName);
            LLJTran thumbnailLLJ = new LLJTran(thumbnailImageFile);
            thumbnailLLJ.read(LLJTran.READ_ALL, true);
            
            try (OutputStream output = new BufferedOutputStream(new FileOutputStream(thumbnailFileName))){   
                // Transform image
                
                thumbnailLLJ.transform(operation, LLJTran.OPT_DEFAULTS
                        | LLJTran.OPT_XFORM_ORIENTATION);
                thumbnailLLJ.save(output, LLJTran.OPT_WRITE_ALL);
                thumbnailLLJ.freeMemory();
                String rotation = "unknown";
                switch (operation) {
                    case 7:
                        rotation = "left";
                        break;
                    default:
                        rotation = "unknown " + operation;
                }
                System.out.println("File " + thumbnailFileName + " rotated " + rotation);
                
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            } finally {
                llj.freeMemory();
            }

        } catch (Exception e) {

            System.out.println("Exception: " + e.getMessage());
            // Unable to rotate image based on EXIF data
        }
    }

    public BasicFileAttributes getFileAttributes(File file) throws IOException {
        return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
    }

//    private void writeToFile(File originalFile, FileImageOutputStream outputStream) throws IOException, ImageProcessingException {
//        BufferedImage originalImage = ImageIO.read(originalFile);
////        Metadata metadata = ImageMetadataReader.readMetadata(fileToWriteTo);
//        ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("jpeg").next();
//        this.iwp = writer.getDefaultWriteParam();
//        this.iwp.setCompressionMode(2);
//        this.iwp.setCompressionQuality(0.25F);
//        writer.setOutput(outputStream);
//        IIOImage image = new IIOImage(originalImage, null, (IIOMetadata)null);
//        writer.write(image.getMetadata(), image, this.iwp);
//        LOGGER.info("Creating thumbnail file for: " + originalFile.getPath() + ", with size: " + originalFile.length());
//    }
    
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
        return file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()) 
                + Constants.THUMBNAILS_DIR + File.separator + file.getName();
    }

    public void createThumbnailDirectory(File directory) {
        File thumbnailDirectory = new File(directory.getPath() + File.separator + Constants.THUMBNAILS_DIR);
        if (!thumbnailDirectory.exists()) {
            LOGGER.info("Creating thumbnail directory [%s]%n", thumbnailDirectory.getName());
            boolean result = thumbnailDirectory.mkdir();
            if (result) {
                LOGGER.info("Thumbnail directory created [%s]%n", thumbnailDirectory.getPath());
            } else {
                LOGGER.info("ERROR: Failed to create thumbnail directory created [%s]%n", thumbnailDirectory.getPath());
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