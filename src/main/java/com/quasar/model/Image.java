package com.quasar.model;

import java.util.UUID;

public class Image
{
	private String id;
	private String name;
	
	public Image(String name)
	{
		this.name = name;
		id = UUID.randomUUID().toString();
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}