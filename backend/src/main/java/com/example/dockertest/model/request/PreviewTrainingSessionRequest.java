package com.example.dockertest.model.request;


public class PreviewTrainingSessionRequest {
    private Integer calories;
    private Integer muscleGroupId;

    public PreviewTrainingSessionRequest(Integer calories, Integer muscleGroupId) {
        this.calories = calories;
        this.muscleGroupId = muscleGroupId;
    }

    public PreviewTrainingSessionRequest() {

    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getMuscleGroupId() {
        return muscleGroupId;
    }

    public void setMuscleGroupId(Integer muscleGroupId) {
        this.muscleGroupId = muscleGroupId;
    }
}
