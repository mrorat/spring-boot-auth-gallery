package com.quasar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import mediautil.image.jpeg.AbstractImageInfo;
import mediautil.image.jpeg.Entry;
import mediautil.image.jpeg.Exif;
import mediautil.image.jpeg.LLJTran;

public class LoselessRotationTest {
	
	public enum ROTATION {
		ROTATE_90_DEG,
		ROTATE_270_DEG,
		TAKE_FROM_EXIF
	}

	@Test
	public void rotate360deg() {
        rotate("c:/code/gallery/src/test/resources/IMG_3065.JPG", "c:/code/gallery/src/test/resources/IMG_3065_90.JPG", ROTATION.ROTATE_90_DEG);
        rotate("c:/code/gallery/src/test/resources/IMG_3065.JPG", "c:/code/gallery/src/test/resources/IMG_3065_from_exif.JPG", ROTATION.TAKE_FROM_EXIF);
		rotate("c:/code/gallery/src/test/resources/IMG_3065_90.JPG", "c:/code/gallery/src/test/resources/IMG_3065_180.JPG", ROTATION.ROTATE_90_DEG);
		rotate("c:/code/gallery/src/test/resources/IMG_3065_180.JPG", "c:/code/gallery/src/test/resources/IMG_3065_270.JPG", ROTATION.ROTATE_90_DEG);
		rotate("c:/code/gallery/src/test/resources/IMG_3065_270.JPG", "c:/code/gallery/src/test/resources/IMG_3065_360.JPG", ROTATION.ROTATE_90_DEG);
	}
		
		
	private void rotate(String inputFile, String outputFile, ROTATION rotation) {
		
		try {
		    // Read image EXIF data
		    File imageFile = new File(inputFile);
			LLJTran llj = new LLJTran(imageFile);
		    llj.read(LLJTran.READ_INFO, true);
		    AbstractImageInfo<?> imageInfo = llj.getImageInfo();
		    if (!(imageInfo instanceof Exif))
		        throw new Exception("Image has no EXIF data");

		    int operation = 0;
		    switch (rotation) {
		    	case ROTATE_90_DEG:
		    		operation = LLJTran.ROT_90;
		    		break;
		    	case ROTATE_270_DEG:
		    		operation = LLJTran.ROT_270;
		    		break;
		    	case TAKE_FROM_EXIF:
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
		    		break;
		    }		    
		    
		    if (operation == 0)
		        throw new Exception("Image orientation is already correct");

		    
			try (OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile))){   
		        // Transform image
		        llj.read(LLJTran.READ_ALL, true);
		        llj.transform(operation, LLJTran.OPT_DEFAULTS
		                | LLJTran.OPT_XFORM_ORIENTATION);

		        llj.save(output, LLJTran.OPT_WRITE_ALL);

		    } finally {
		        llj.freeMemory();
		    }

		} catch (Exception e) {
		    // Unable to rotate image based on EXIF data
		    
		}
	}

}
