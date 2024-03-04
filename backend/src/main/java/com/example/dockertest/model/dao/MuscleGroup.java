package com.example.dockertest.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "muscle_group")
public class MuscleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToMany
    @JoinTable(
      name = "muscle_group_2_exercise",
      joinColumns = @JoinColumn(name = "muscle_group_id"),
      inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    @JsonIgnore
    private Set<Exercise> exercises;

    @ManyToMany
    @JoinTable(
      name = "muscle_group_2_warmup",
      joinColumns = @JoinColumn(name = "muscle_group_id"),
      inverseJoinColumns = @JoinColumn(name = "warmup_id")
    )
    @JsonIgnore
    private Set<Warmup> warmups;

    @ManyToMany
    @JoinTable(
      name = "muscle_group_2_stretch",
      joinColumns = @JoinColumn(name = "muscle_group_id"),
      inverseJoinColumns = @JoinColumn(name = "stretch_id")
    )
    @JsonIgnore
    private Set<Stretch> stretches;

    public MuscleGroup() {

    }

    public MuscleGroup(String name) {
        this.name = name;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }

    public Set<Warmup> getWarmups() {
        return warmups;
    }

    public void setWarmups(Set<Warmup> warmups) {
        this.warmups = warmups;
    }

    public Set<Stretch> getStretches() {
        return stretches;
    }

    public void setStretches(Set<Stretch> stretches) {
        this.stretches = stretches;
    }
}
