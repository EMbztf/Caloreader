package com.example.dockertest.model.request;

public class UpdateSexRequest {

	private String sex;

	public UpdateSexRequest() {
	}

	public UpdateSexRequest(String sex) {
		super();
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
