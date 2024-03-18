package com.example.dockertest.model.request;

public class UpdatePasswordRequest {

	private String newPassword;
	private String password;

	public UpdatePasswordRequest() {
	}

	public UpdatePasswordRequest(String newPassword, String password) {
		super();
		this.newPassword = newPassword;
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
