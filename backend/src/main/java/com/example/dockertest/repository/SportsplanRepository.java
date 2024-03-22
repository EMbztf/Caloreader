package com.example.dockertest.repository;

import com.example.dockertest.model.dao.Sportsplan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsplanRepository extends JpaRepository<Sportsplan, Integer> {
	Optional<Sportsplan> findById(Integer id);

	List<Sportsplan> findAll();

	List<Sportsplan> findByUserId(Integer userId);
}
