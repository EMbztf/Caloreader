package com.example.dockertest.model.request;

public class UpdateWeightRequest {

	private double weight;

	public UpdateWeightRequest() {
	}

	public UpdateWeightRequest(double weight) {
		super();
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
