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
@Table(name = "sports_plan")
public class Sportsplan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "calories")
	private int calories;

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
		name = "sports_plan_2_sports",
		joinColumns = @JoinColumn(name = "sports_plan_id"),
		inverseJoinColumns = @JoinColumn(name = "sports_id")
	)
	private List<Sports> sports;

	public Sportsplan() {
	}

	public Sportsplan(String name, int calories, double estimatedDuration, User user, List<Sports> sports, Date date) {
		this.name = name;
		this.calories = calories;
		this.estimatedDuration = estimatedDuration;
		this.user = user;
		this.sports = sports;
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

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
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


	public List<Sports> getSports() {
		return sports;
	}

	public void setSports(List<Sports> sports) {
		this.sports = sports;
	}
}
