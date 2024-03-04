package com.example.dockertest.repository;

import com.example.dockertest.model.dao.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StretchRepository extends JpaRepository<Exercise, Integer> {
    Optional<Exercise> findById(Integer id);

    List<Exercise> findAll();
}
