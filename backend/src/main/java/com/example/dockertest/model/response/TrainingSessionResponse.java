package com.example.dockertest.model.response;

import com.example.dockertest.model.dao.Exercise;
import com.example.dockertest.model.dao.Stretch;
import com.example.dockertest.model.dao.Warmup;

import java.util.List;

public class TrainingSessionResponse {
    private double estimatedTime;
    private List<Exercise> exercises;
    private List<Warmup> warmups;
    private List<Stretch> stretches;

    public TrainingSessionResponse(double estimatedTime, List<Exercise> exercises, List<Warmup> warmups, List<Stretch> stretches) {
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

    public List<Warmup> getWarmups() {
        return warmups;
    }

    public void setWarmups(List<Warmup> warmups) {
        this.warmups = warmups;
    }

    public List<Stretch> getStretches() {
        return stretches;
    }

    public void setStretches(List<Stretch> stretches) {
        this.stretches = stretches;
    }
}
