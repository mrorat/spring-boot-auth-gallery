package com.quasar;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.quasar.util.CrcUtil;

public class FileCRCTest {

    @Test
	public void crcComparisonTest() {
		try {
            long file1aCRC = CrcUtil.getFileCyclicRedundancyCheck("crc/crc_test_1a.txt");
            long file1bCRC = CrcUtil.getFileCyclicRedundancyCheck("crc/crc_test_1b.txt");
            long file0CRC = CrcUtil.getFileCyclicRedundancyCheck("crc/crc_test_0.txt");
            long file2CRC = CrcUtil.getFileCyclicRedundancyCheck("crc/crc_test_2s.txt");
            
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
            CrcUtil.getFileCyclicRedundancyCheck("IMG_3065_180.JPG");
            long t1 = System.currentTimeMillis();
            CrcUtil.getFileCyclicRedundancyCheck("IMG_3065_180.JPG");
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
