package com.quasar;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GalleryApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(GalleryApplication.class);
    private static String galleryHomeDirectory;

    public static void main(String[] args) throws IOException {

        CommandLineParser cmdLineParser = new DefaultParser();
        try {
            CommandLine cmdLine = cmdLineParser.parse(getCommandLineOptions(), args);
            if (cmdLine.hasOption("homeDirectory"))
            {
                File dir = new File(cmdLine.getOptionValue("homeDirectory"));
                if (!dir.exists() || !dir.isDirectory()) {
                    throw new IOException("Specified location [" + args[1] + "] does not exist or is not a directory");
                }
                
                // TODO check if we have access to this directory
                
                galleryHomeDirectory = args[1];
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        LOGGER.info("Gallery home directory set to: " + galleryHomeDirectory);

        SpringApplication.run(GalleryApplication.class, args);
    }

    private static Options getCommandLineOptions() {
        Options options = new Options();
        options.addOption(new Option("homeDirectory", true, "gallery's root directory"));
        return options;
    }

    public static String getGalleryHomeDirectory() {
        return galleryHomeDirectory;
    }
}