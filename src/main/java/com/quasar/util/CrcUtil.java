package com.quasar.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;

public class CrcUtil {

	public static long getFileCyclicRedundancyCheck(String filePath) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		MappedByteBuffer buffer = inputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, inputStream.getChannel().size());
		CRC32 crc = new CRC32();
		crc.update(buffer);
		inputStream.close();
		return crc.getValue();
	}
}