package com.example.dockertest.repository;

import com.example.dockertest.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Optional<Exercise> findById(Integer aLong);

    List<Exercise> findAll();

}
