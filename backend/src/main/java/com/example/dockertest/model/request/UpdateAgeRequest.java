package com.example.dockertest.model.request;

public class UpdateAgeRequest {

	private int age;

	public UpdateAgeRequest() {
	}


	public UpdateAgeRequest(int age) {
		super();
		this.age = age;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}
}
