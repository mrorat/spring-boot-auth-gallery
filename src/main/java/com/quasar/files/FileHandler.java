package com.quasar.files;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.quasar.model.Album;
import com.quasar.repository.Repository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import org.springframework.stereotype.Service;

@Service
public class FileHandler {
    private ExecutorService executor = null;
    private ImageWriteParam iwp;

    public FileHandler() {
        this.executor = Executors.newFixedThreadPool(5);
    }

    public InputStreamWithSize getStreamWithSize(String albumId, String imageId) throws IOException {
        File f = new File(Repository.getImageById(albumId, imageId).getPath());
        return new InputStreamWithSize(new FileInputStream(f), Files.size(f.toPath()));
    }

    public String getFileContentAsBase64(String albumId, String imageId) throws IOException {
        System.out.println(Instant.now() + " Request to get image id: " + imageId);
        String filePath = Repository.getImageById(albumId, imageId).getPath();
        File f = new File(filePath);
        InputStream finput = new FileInputStream(f);
        Throwable var6 = null;

        String var9;
        try {
            byte[] imageBytes = new byte[(int)Files.size(f.toPath())];
            int bytesRead = finput.read(imageBytes, 0, imageBytes.length);
            if (bytesRead != imageBytes.length) {
                System.out.printf("ERROR: File [%s] was read and it's size [%d] did not match bytes read [%d]%n", f.getPath(), imageBytes.length, bytesRead);
            }

            var9 = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Throwable var18) {
            var6 = var18;
            throw var18;
        } finally {
            if (finput != null) {
                if (var6 != null) {
                    try {
                        finput.close();
                    } catch (Throwable var17) {
                        var6.addSuppressed(var17);
                    }
                } else {
                    finput.close();
                }
            }

        }

        return var9;
    }

    public String getFileContentAsBase64Thumbnail(String albumId, String imageId) throws IOException {
    	long start = System.currentTimeMillis();
        System.out.println(Instant.now() + " Request to get thumbnail for image id: " + imageId);
        String filePath = Repository.getImageById(albumId, imageId).getThumbnailPath();
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
            if (!(new File(thumbnailFileName)).exists()) {
                System.out.println(Instant.now() + " Creating thumbnail file for: " + file.getPath());
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

    private void writeToFile(File originalFile, FileImageOutputStream outputStream) throws IOException, ImageProcessingException {
        BufferedImage originalImage = ImageIO.read(originalFile);
        Metadata metadata = ImageMetadataReader.readMetadata(originalFile);
        System.out.println(metadata);
        ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName("jpeg").next();
        writer.setOutput(outputStream);
        this.iwp = writer.getDefaultWriteParam();
        this.iwp.setCompressionMode(2);
        this.iwp.setCompressionQuality(0.25F);
        IIOImage image = new IIOImage(originalImage, null, (IIOMetadata)null);
        writer.write(image.getMetadata(), image, this.iwp);
        System.out.println(Instant.now() + " Creating thumbnail file for: " + originalFile.getPath() + ", with size: " + image.getRenderedImage().getData().getDataBuffer().getSize());
    }

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
            Throwable var4 = null;

            try {
                fw.write(album.getAlbumid());
                fw.flush();
            } catch (Throwable var14) {
                var4 = var14;
                throw var14;
            } finally {
                if (fw != null) {
                    if (var4 != null) {
                        try {
                            fw.close();
                        } catch (Throwable var13) {
                            var4.addSuppressed(var13);
                        }
                    } else {
                        fw.close();
                    }
                }

            }
        } catch (IOException var16) {
            var16.printStackTrace();
        }

    }
}
