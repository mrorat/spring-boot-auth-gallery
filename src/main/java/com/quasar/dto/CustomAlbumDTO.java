package com.quasar.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CustomAlbumDTO {

	@NotNull
	@NotEmpty
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}