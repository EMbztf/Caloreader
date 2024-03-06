package com.example.dockertest.model.dao;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "warmup")
public class Warmup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "MET")
    private double MET;

    @Column(name = "duration")
    private double duration;

    @Column(name = "repetitions")
    private int repetitions;

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "sibling_warmup_id")
    private Integer siblingWarmupId;

    @ManyToMany(mappedBy = "warmups")
    private Set<MuscleGroup> muscleGroups;

    public Warmup() {

    }

    public Warmup(String name, double MET, double duration, int repetitions, String videoPath, Integer siblingWarmupId) {
        this.name = name;
        this.MET = MET;
        this.duration = duration;
        this.repetitions = repetitions;
        this.videoPath = videoPath;
        this.siblingWarmupId = siblingWarmupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMET() {
        return MET;
    }

    public void setMET(double MET) {
        this.MET = MET;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Integer getSiblingWarmupId() {
        return siblingWarmupId;
    }

    public void setSiblingWarmupId(Integer siblingWarmupId) {
        this.siblingWarmupId = siblingWarmupId;
    }

    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(Set<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }
}
