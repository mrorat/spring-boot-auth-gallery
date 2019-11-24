package com.quasar;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class FileCRCTest {


	public void crcComparisonTest() {
		try {
            long file1aCRC = TestCRC.checksumMappedFile("crc/crc_test_1a.txt");
            long file1bCRC = TestCRC.checksumMappedFile("crc/crc_test_1b.txt");
            long file0CRC = TestCRC.checksumMappedFile("crc/crc_test_0.txt");
            long file2CRC = TestCRC.checksumMappedFile("crc/crc_test_2s.txt");
            
            assertTrue(file1aCRC == file1bCRC);
            assertFalse(file1aCRC != file0CRC);
            assertFalse(file1bCRC != file0CRC);
            assertFalse(file1aCRC != file2CRC);
            assertFalse(file1bCRC != file2CRC);
            assertFalse(file0CRC != file2CRC);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	@Test
	public void performanceTest() {
	    try {
	        long t0 = System.currentTimeMillis();
            TestCRC.checksumMappedFile("IMG_3065_180.JPG");
            long t1 = System.currentTimeMillis();
            TestCRC.checksumMappedFile2("IMG_3065_180.JPG");
            long t2 = System.currentTimeMillis();
            
            System.out.println("byte by byte: " + (t1-t0));
            System.out.println("buffer: " + (t2-t1));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

}
