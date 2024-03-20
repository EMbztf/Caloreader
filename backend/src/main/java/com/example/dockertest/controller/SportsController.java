package com.example.dockertest.controller;

import com.example.dockertest.model.dao.Sports;
import com.example.dockertest.model.dao.User;
import com.example.dockertest.model.error.ErrorMessage;
import com.example.dockertest.model.request.PreviewSportsSessionRequest;
import com.example.dockertest.model.response.SportsSessionResponse;
import com.example.dockertest.repository.SportsRepository;
import com.example.dockertest.repository.UserRepository;
import com.example.dockertest.security.services.UserDetailsImpl;
import com.example.dockertest.util.Calculator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SportsController {

	@Autowired
	private SportsRepository sportsRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Retrieves the authenticated user.
	 *
	 * @return the authenticated user
	 */
	private User getAuthenticatedUser() {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findById(user.getId()).get();
	}

	@GetMapping("/sports")
	public ResponseEntity<List<Sports>> getAllSports() {
		List<Sports> sports = sportsRepository.findAllByOrderByName();
		return ResponseEntity.ok(sports);
	}

	/**
	 * Generates a new training session for the authenticated user.
	 *
	 * @return the training session
	 */
	@PostMapping("/previewSportsSession")
	public ResponseEntity<?> postPreviewSportsSession(@RequestBody PreviewSportsSessionRequest request) {
		try {
			ResponseEntity<?> response = validatePreviewSportsSessionRequest(request);
			if (response != null) {
				return response;
			}

			List<Sports> sports = getSportsWithDurationFromRequest(request);
			double estimatedDuration = calculateEstimatedDurationFromSports(sports);

			SportsSessionResponse sportsSessionResponse = new SportsSessionResponse(estimatedDuration, sports);
			return ResponseEntity.ok(sportsSessionResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private double calculateEstimatedDurationFromSports(List<Sports> sports) {
		double estimatedDuration = 0;
		for (Sports sport : sports) {
			estimatedDuration += sport.getDuration();
		}
		return estimatedDuration;
	}

	private List<Sports> getSportsWithDurationFromRequest(PreviewSportsSessionRequest request) {
		List<Sports> sports = sportsRepository.findAllById(request.getSports());
		int calories = request.getCalories() / request.getSports().size();

		for (Sports sport : sports) {
			double duration = Math.round(getDurationFromSportsAndCalories(sport, calories));
			sport.setDuration(duration);
		}

		return sports;
	}

	private double getDurationFromSportsAndCalories(Sports sport, int calories) {
		User user = getAuthenticatedUser();
		double correctedMET = Calculator.calculateCorrectedMET(sport.getMET(), user.getWeight(), user.getHeight(), user.getAge(), user.getSex());
		return (calories * 60) / (correctedMET * 3.5 * user.getWeight() / 200);
	}

	private ResponseEntity<?> validatePreviewSportsSessionRequest(PreviewSportsSessionRequest request) {
		boolean hasError = false;
		ErrorMessage error = new ErrorMessage();
		if (request.getCalories() < 0) {
			hasError = true;
			error.setMessage("Calories must be greater than 0");
		}
		if (request.getSports() == null || request.getSports().isEmpty()) {
			hasError = true;
			error.setMessage("No sports selected");
		}
		if (hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}
}
