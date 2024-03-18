package com.example.dockertest.model.request;

public class UpdateHeightRequest {

	private double height;

	public UpdateHeightRequest() {
	}


	public UpdateHeightRequest(double height) {
		super();
		this.height = height;
	}


	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}

}
