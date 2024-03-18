package com.example.dockertest.model.request;

import java.util.List;

public class TrainingSessionRequest {

	private int calories;
	private int muscleGroupId;
	private List<Integer> exercises;

	public TrainingSessionRequest() {
	}

	public TrainingSessionRequest(int calories, int muscleGroupId, List<Integer> exercises) {
		this.calories = calories;
		this.muscleGroupId = muscleGroupId;
		this.exercises = exercises;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public int getMuscleGroupId() {
		return muscleGroupId;
	}

	public void setMuscleGroupId(int muscleGroupId) {
		this.muscleGroupId = muscleGroupId;
	}

	public List<Integer> getExercises() {
		return exercises;
	}

	public void setExercises(List<Integer> exercises) {
		this.exercises = exercises;
	}
}
