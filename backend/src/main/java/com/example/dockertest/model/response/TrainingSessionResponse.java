package com.example.dockertest.model.response;

import com.example.dockertest.model.dao.Exercise;

import java.util.List;

public class TrainingSessionResponse {
    private double estimatedTime;
    private List<Exercise> exercises;
    private List<Exercise> warmups;
    private List<Exercise> stretches;

    public TrainingSessionResponse(double estimatedTime, List<Exercise> exercises, List<Exercise> warmups, List<Exercise> stretches) {
        this.estimatedTime = estimatedTime;
        this.exercises = exercises;
        this.warmups = warmups;
        this.stretches = stretches;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Exercise> getWarmups() {
        return warmups;
    }

    public void setWarmups(List<Exercise> warmups) {
        this.warmups = warmups;
    }

    public List<Exercise> getStretches() {
        return stretches;
    }

    public void setStretches(List<Exercise> stretches) {
        this.stretches = stretches;
    }
}
