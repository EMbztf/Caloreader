package com.example.dockertest.repository;

import com.example.dockertest.model.dao.Sports;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsRepository extends JpaRepository<Sports, Integer> {
	Optional<Sports> findById(Integer id);

	List<Sports> findAll();

	List<Sports> findAllByOrderByName();
}
