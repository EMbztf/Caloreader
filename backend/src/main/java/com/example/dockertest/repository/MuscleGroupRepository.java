package com.example.dockertest.repository;

import com.example.dockertest.model.dao.MuscleGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Integer> {
    Optional<MuscleGroup> findById(Integer aInt);

    List<MuscleGroup> findAll();
}
