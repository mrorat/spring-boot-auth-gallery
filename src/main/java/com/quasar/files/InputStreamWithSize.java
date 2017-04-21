package com.quasar.files;

import java.io.InputStream;

public class InputStreamWithSize
{
	private InputStream inputStream;
	private long size;
	
	public InputStreamWithSize(InputStream inputStream, long l)
	{
		this.inputStream = inputStream;
		this.size = l;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}

	public long getSize()
	{
		return size;
	}
}