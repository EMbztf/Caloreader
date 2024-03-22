package com.example.dockertest.controller;

import com.example.dockertest.model.dao.Sports;
import com.example.dockertest.model.dao.Sportsplan;
import com.example.dockertest.model.dao.User;
import com.example.dockertest.model.error.ErrorMessage;
import com.example.dockertest.model.request.PreviewSportsSessionRequest;
import com.example.dockertest.model.request.SportsplanRequest;
import com.example.dockertest.model.response.SportsSessionResponse;
import com.example.dockertest.repository.SportsRepository;
import com.example.dockertest.repository.SportsplanRepository;
import com.example.dockertest.repository.UserRepository;
import com.example.dockertest.security.services.UserDetailsImpl;
import com.example.dockertest.util.Calculator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	private SportsplanRepository sportsplanRepository;

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

	/**
	 * Gets all sports
	 *
	 * @return the list of sports
	 */
	@GetMapping("/sports")
	public ResponseEntity<List<Sports>> getAllSports() {
		List<Sports> sports = sportsRepository.findAllByOrderByName();
		return ResponseEntity.ok(sports);
	}

	/**
	 * Gets all sportsplans for the authenticated user
	 *
	 * @return the list of sportsplans
	 */
	@GetMapping("/sportsplans")
	public ResponseEntity<List<Sportsplan>> getAllSportsplans() {
		User user = getAuthenticatedUser();
		List<Sportsplan> sportsplans = sportsplanRepository.findByUserId(user.getId());
		return ResponseEntity.ok(sportsplans);
	}

	/**
	 * Gets a sportsplan by its id
	 *
	 * @param id the id of the sportsplan
	 * @return the details of the sportsplan
	 */
	@GetMapping("/sportsplans/{id}")
	public ResponseEntity<Sportsplan> getSportsplanById(@PathVariable Integer id) {
		Optional<Sportsplan> sportsplan = sportsplanRepository.findById(id);
		if (sportsplan.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		User user = getAuthenticatedUser();
		if (!user.getId().equals(sportsplan.get().getUser().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		sportsplan.get().setSports(getSportsWithDurationFromCalories(sportsplan.get().getCalories(), sportsplan.get().getSports()));
		return ResponseEntity.ok(sportsplan.get());
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
			List<Sports> sports = sportsRepository.findAllById(request.getSports());
			sports = getSportsWithDurationFromCalories(request.getCalories(), sports);
			double estimatedDuration = calculateEstimatedDurationFromSports(sports);

			SportsSessionResponse sportsSessionResponse = new SportsSessionResponse(estimatedDuration, sports);
			return ResponseEntity.ok(sportsSessionResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Generates a new sportsplan for the authenticated user.
	 *
	 * @return         	the sportsplan
	 */
	@PostMapping("/sportsplan")
	public ResponseEntity<?> postSportsplan(@RequestBody SportsplanRequest request) {
		try {
			ResponseEntity<?> response = validateSportsplanRequest(request);
			if (response != null) {
				return response;
			}
			List<Sports> sports = sportsRepository.findAllById(request.getSports());
			sports = getSportsWithDurationFromCalories(request.getCalories(), sports);
			Date currentDate = new Date();
			String name = request.getCalories() + " calories " + request.getSports().size() + " sports";
			double estimatedDuration = calculateEstimatedDurationFromSports(sports);
			Sportsplan sportsplan = new Sportsplan(name, request.getCalories(), estimatedDuration, getAuthenticatedUser(), sports, currentDate);
			Sportsplan savedSportsplan = sportsplanRepository.save(sportsplan);
			return ResponseEntity.ok(savedSportsplan);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Deletes the sportsplan by the provided ID.
	 *
	 * @return         	the sportsplan
	 */
	@DeleteMapping("/sportsplans/{id}")
	public ResponseEntity<HttpStatus> deleteSportsplan(@PathVariable Integer id) {
		User authenticatedUser = getAuthenticatedUser();
		Optional<Sportsplan> sportsplan = sportsplanRepository.findById(id);
		if (sportsplan.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (!Objects.equals(authenticatedUser.getId(), sportsplan.get().getUser().getId())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		try {
			sportsplanRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Calculates the estimated duration from the provided sports.
	 *
	 * @param sports the list of sports
	 * @return the estimated duration
 	*/
	private double calculateEstimatedDurationFromSports(List<Sports> sports) {
		double estimatedDuration = 0;
		for (Sports sport : sports) {
			estimatedDuration += sport.getDuration();
		}
		return estimatedDuration;
	}

	/**
	 * Calculates the duration from the provided calories and sports.
	 *
	 * @param sports the list of sports
	 * @return the estimated duration
	 */
	private List<Sports> getSportsWithDurationFromCalories(int calories, List<Sports> sports) {
		int dividedCalories = calories / sports.size();

		for (Sports sport : sports) {
			double duration = Math.round(getDurationFromSportsAndCalories(sport, dividedCalories));
			sport.setDuration(duration);
		}

		return sports;
	}

	/**
	 * Calculates the duration from the provided calories and sports.
	 *
	 * @param sport the sport
	 * @param calories the calories
	 * @return the estimated duration
	 */
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
		} else {
			for (Integer sportId : request.getSports()) {
				Sports sport = sportsRepository.findById(sportId).orElse(null);
				if (sport == null) {
					hasError = true;
					error.setMessage("Invalid sport id");
					break;
				}
			}
		}
		if (hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}

	private ResponseEntity<?> validateSportsplanRequest(SportsplanRequest request) {
		boolean hasError = false;
		ErrorMessage error = new ErrorMessage();
		if (request.getCalories() < 0) {
			hasError = true;
			error.setMessage("Calories must be greater than 0");
		}
		if (request.getSports() == null || request.getSports().isEmpty()) {
			hasError = true;
			error.setMessage("No sports selected");
		} else {
			for (Integer sportId : request.getSports()) {
				Sports sport = sportsRepository.findById(sportId).orElse(null);
				if (sport == null) {
					hasError = true;
					error.setMessage("Invalid sport id");
					break;
				}
			}
		}
		if (hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}
}
