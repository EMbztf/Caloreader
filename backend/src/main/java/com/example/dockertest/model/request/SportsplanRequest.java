package com.example.dockertest.model.request;

import java.util.List;

public class SportsplanRequest {

	private int calories;
	List<Integer> sports;

	public SportsplanRequest(int calories, List<Integer> sports) {
		this.calories = calories;
		this.sports = sports;
	}

	public List<Integer> getSports() {
		return sports;
	}


	public void setSports(List<Integer> sports) {
		this.sports = sports;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}
}
