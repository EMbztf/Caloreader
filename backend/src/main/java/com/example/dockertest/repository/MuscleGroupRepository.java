package com.example.dockertest.repository;

import com.example.dockertest.model.Exercise;
import com.example.dockertest.model.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Integer> {
    Optional<MuscleGroup> findById(Integer aInt);

    List<MuscleGroup> findAll();
}
