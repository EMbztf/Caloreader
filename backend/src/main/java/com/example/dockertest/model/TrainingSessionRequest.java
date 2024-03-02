package com.example.dockertest.model;


public class TrainingSessionRequest {
    private Integer calories;
    private Integer muscleGroupId;
    private Double weight;
    private Integer height;
    private Integer age;
    private String gender;

    public TrainingSessionRequest(Integer calories, Integer muscleGroupId, Double weight, Integer height, Integer age, String gender) {
        this.calories = calories;
        this.muscleGroupId = muscleGroupId;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
    }

    public TrainingSessionRequest() {

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
