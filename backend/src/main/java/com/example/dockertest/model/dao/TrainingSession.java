package com.example.dockertest.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "training_session")
public class TrainingSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "estimated_duration")
	private double estimatedDuration;

	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	@JsonIgnore
	private User user;

	@Column(name = "date")
	private Date date;

	@ManyToMany
	@JoinTable(
		name = "training_session_2_exercise",
		joinColumns = @JoinColumn(name = "training_session_id"),
		inverseJoinColumns = @JoinColumn(name = "exercise_id")
	)
	private List<Exercise> exercises;

	public TrainingSession() {
	}

	public TrainingSession(String name, double estimatedDuration, User user, List<Exercise> exercises, Date date) {
		this.name = name;
		this.estimatedDuration = estimatedDuration;
		this.user = user;
		this.exercises = exercises;
		this.date = date;
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


	public double getEstimatedDuration() {
		return estimatedDuration;
	}


	public void setEstimatedDuration(double estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public List<Exercise> getExercises() {
		return exercises;
	}


	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}
}
