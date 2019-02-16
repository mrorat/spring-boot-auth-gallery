package com.quasar.controllers.dto;

public class Rotation {
	
	private int degrees;

	public int getDegrees() {
		return degrees;
	}

	public void setDegrees(int degrees) {
		this.degrees = degrees;
	}

	@Override
	public String toString() {
		return "Rotation [degrees=" + degrees + "]";
	}
}