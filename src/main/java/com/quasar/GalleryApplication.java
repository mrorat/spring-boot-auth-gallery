package com.quasar;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GalleryApplication {
    private static String galleryHomeDirectory;

    public GalleryApplication() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            File dir = new File(args[1]);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IOException("Specified location [" + args[1] + "] does not exist or is not a directory");
            }

            galleryHomeDirectory = args[1];
        }

        SpringApplication.run(GalleryApplication.class, args);
    }

    public static String getGalleryHomeDirectory() {
        return galleryHomeDirectory;
    }
}
