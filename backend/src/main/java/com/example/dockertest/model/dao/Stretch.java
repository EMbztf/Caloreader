package com.example.dockertest.model.dao;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "stretch")
public class Stretch {
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

    @Column(name = "sibling_stretch_id")
    private Integer siblingStretchId;

    @ManyToMany(mappedBy = "stretches")
    private Set<MuscleGroup> muscleGroups;

    public Stretch() {

    }

    public Stretch(String name, double MET, double duration, int repetitions, String videoPath, Integer siblingStretchId) {
        this.name = name;
        this.MET = MET;
        this.duration = duration;
        this.repetitions = repetitions;
        this.videoPath = videoPath;
        this.siblingStretchId = siblingStretchId;
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

    public Integer getSiblingStretchId() {
        return siblingStretchId;
    }

    public void setSiblingStretchId(Integer siblingStretchId) {
        this.siblingStretchId = siblingStretchId;
    }

    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(Set<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }
}
