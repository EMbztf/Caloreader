package com.example.dockertest.repository;

import com.example.dockertest.model.dao.TrainingSession;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {
	Optional<TrainingSession> findById(Integer id);

	List<TrainingSession> findAll();

	List<TrainingSession> findByUserId(Integer userId);
}
