package com.example.dockertest.model.response;

import com.example.dockertest.model.dao.Sports;
import java.util.List;

public class SportsSessionResponse {
	private double estimatedTime;
	private List<Sports> sports;

	public SportsSessionResponse(double estimatedTime, List<Sports> sports) {
		this.estimatedTime = estimatedTime;
		this.sports = sports;
	}

	public double getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public List<Sports> getSports() {
		return sports;
	}

	public void setSports(List<Sports> sports) {
		this.sports = sports;
	}
}
