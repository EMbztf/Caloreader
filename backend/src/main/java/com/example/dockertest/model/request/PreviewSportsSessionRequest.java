package com.example.dockertest.model.request;

import java.util.List;

public class PreviewSportsSessionRequest {
	private int calories;
	private List<Integer> sports;

	public PreviewSportsSessionRequest(int calories, List<Integer> sports) {
		this.calories = calories;
		this.sports = sports;
	}

	public PreviewSportsSessionRequest() {

	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public List<Integer> getSports() {
		return sports;
	}

	public void setSports(List<Integer> sports) {
		this.sports = sports;
	}
}
