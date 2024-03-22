package com.example.dockertest.repository;

import com.example.dockertest.model.dao.Exercise;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Optional<Exercise> findById(Integer id);

    List<Exercise> findAll();
}
