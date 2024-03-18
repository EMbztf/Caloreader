package com.example.dockertest.controller;

import com.example.dockertest.model.dao.Exercise;
import com.example.dockertest.model.dao.MuscleGroup;
import com.example.dockertest.model.dao.TrainingSession;
import com.example.dockertest.model.dao.User;
import com.example.dockertest.model.request.PreviewTrainingSessionRequest;
import com.example.dockertest.model.request.TrainingSessionRequest;
import com.example.dockertest.model.response.TrainingSessionResponse;
import com.example.dockertest.repository.ExerciseRepository;
import com.example.dockertest.repository.MuscleGroupRepository;
import com.example.dockertest.repository.TrainingSessionRepository;
import com.example.dockertest.repository.UserRepository;
import com.example.dockertest.security.services.UserDetailsImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
public class TrainingSessionController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ExerciseRepository exerciseRepository;

	@Autowired
	TrainingSessionRepository trainingSessionRepository;

	@Autowired
	MuscleGroupRepository muscleGroupRepository;


	/**
	 * Retrieves the authenticated user.
	 *
	 * @return         the authenticated user
	 */
	private User getAuthenticatedUser() {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findById(user.getId()).get();
	}

	/**
	 * Retrieves the list of training sessions associated with the authenticated user.
	 *
	 * @return         	the list of training session ids
	 */
	@GetMapping("/trainingSessions")
	public ResponseEntity<List<TrainingSession>> getTrainingSessions() {
		User authenticatedUser = getAuthenticatedUser();
		List<TrainingSession> trainingSessions = trainingSessionRepository.findByUserId(authenticatedUser.getId());
//		trainingSessions.forEach(trainingSession -> trainingSession.setExercises(null));
		return ResponseEntity.ok(trainingSessions);
	}

	/**
	 * Retrieves the training session associated with the authenticated user.
	 *
	 * @return         	the training session
	 */
	@GetMapping("/trainingSessions/{id}")
	public ResponseEntity<TrainingSession> getTrainingSessionsById(@PathVariable Integer id) {
		User authenticatedUser = getAuthenticatedUser();
		Optional<TrainingSession> trainingSession = trainingSessionRepository.findById(id);
		if (trainingSession.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		if (!Objects.equals(authenticatedUser.getId(), trainingSession.get().getUser().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return trainingSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}


	/**
	 * Generates a new training session for the authenticated user.
	 *
	 * @return         	the training session
	 */
	@PostMapping("/previewTrainingSessions")
	public ResponseEntity<TrainingSessionResponse> postPreviewTrainingSessions(
		@RequestBody PreviewTrainingSessionRequest request) {
		try {
			User authenticatedUser = getAuthenticatedUser();
			validatePreviewTrainingSessionRequest(request);
			Optional<MuscleGroup> muscleGroup = muscleGroupRepository.findById(request.getMuscleGroupId());
			TrainingSessionResponse response = null;
			if (muscleGroup.isPresent()) {
				Set<Exercise> exercises = muscleGroup
					.get()
					.getExercises()
					.stream()
					.filter(exercise -> !exercise.isWarmup() && !exercise.isStretch())
					.collect(Collectors.toSet());
				if (exercises.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}

				List<Exercise> warmups = muscleGroup.get().getExercises().stream().filter(Exercise::isWarmup).toList();
				List<Exercise> stretches = muscleGroup
					.get()
					.getExercises()
					.stream()
					.filter(Exercise::isStretch)
					.toList();
				List<Exercise> defaultExercises = Stream
					.of(warmups, stretches)
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
				double defaultCalories = calculateCaloriesForExercises(defaultExercises, authenticatedUser);
				request.setCalories((int) (request.getCalories() - defaultCalories));

				List<Exercise> sessionExercises = getTrainingSessionExercises(request, exercises, authenticatedUser);
				double estimatedDuration = calculateEstimatedDuration(muscleGroup.get().getExercises().stream().toList());
				double estimatedTime = calculateEstimatedTime(warmups, sessionExercises, stretches);

				response = new TrainingSessionResponse(estimatedTime, sessionExercises, warmups, stretches);
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Stores a new training session with the exercises provided in the request body.
	 *
	 * @param  request the request body containing the exercises for the training session
	 * @return         the response entity containing the saved training session or an error status
	 */
	@PostMapping("/trainingSessions")
	public ResponseEntity<TrainingSession> postTrainingSessions(@RequestBody TrainingSessionRequest request) {
		try {
			User user = getAuthenticatedUser();
			List<Integer> exercisesId = request.getExercises();
			List<Exercise> exercises = exerciseRepository.findAllById(exercisesId);
			Optional<MuscleGroup> muscleGroup = muscleGroupRepository.findById(request.getMuscleGroupId());
			if(request.getCalories() < 0) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			if(muscleGroup.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			if (exercises.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			List<Exercise> sessionExercises = new ArrayList<>();
			for (Integer exerciseId : exercisesId) {
				Exercise exercise = exercises
					.stream()
					.filter(e -> Objects.equals(e.getId(), exerciseId))
					.findFirst()
					.orElse(null);
				sessionExercises.add(exercise);
			}

			double estimatedDuration = calculateEstimatedDuration(sessionExercises);
			Date currentDate = new Date();
			String name = muscleGroup.get().getName() + " " + request.getCalories() + " calories";
			TrainingSession trainingSession = new TrainingSession(name, estimatedDuration, user, sessionExercises, currentDate);
			TrainingSession savedTrainingSession = trainingSessionRepository.save(trainingSession);
			return new ResponseEntity<>(savedTrainingSession, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Deletes the training session by the provided ID.
	 *
	 * @return         	the training session
	 */
	@DeleteMapping("/trainingSessions/{id}")
	public ResponseEntity<HttpStatus> deleteTrainingSession(@PathVariable Integer id) {
		User authenticatedUser = getAuthenticatedUser();
		Optional<TrainingSession> trainingSession = trainingSessionRepository.findById(id);
		if (trainingSession.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (!Objects.equals(authenticatedUser.getId(), trainingSession.get().getUser().getId())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		try {
			trainingSessionRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private double calculateEstimatedDuration(List<Exercise> exercises) {
		double estimatedDuration = 0;
		double restTime = 30;
		double stretchRestTime = 10;

		for(Exercise exercise : exercises) {
			estimatedDuration += exercise.getDuration() * exercise.getRepetitions();
		}

		estimatedDuration += exercises.size() * restTime;
		estimatedDuration += (exercises.stream().filter(Exercise::isStretch).count() - 1) * stretchRestTime;

		return estimatedDuration;
	}

	/**
	 * Calculate the estimated time for a workout session based on the duration of exercises and rest times.
	 *
	 * @param  warmups    list of warm-up exercises
	 * @param  exercises  list of main exercises
	 * @param  stretches  list of stretching exercises
	 * @return            the total estimated time for the workout session
	 */
	private double calculateEstimatedTime(List<Exercise> warmups, List<Exercise> exercises, List<Exercise> stretches) {
		double estimatedTime = 0;
		double restTime = 30;
		double stretchRestTime = 10;
		List<Exercise> allExercises = Stream.of(warmups, exercises, stretches).flatMap(Collection::stream).toList();
		for (Exercise exercise : allExercises) {
			estimatedTime += exercise.getDuration() * exercise.getRepetitions();
		}

		estimatedTime += warmups.size() * restTime;
		estimatedTime += exercises.size() * restTime;
		estimatedTime += (stretches.size() - 1) * stretchRestTime;

		return estimatedTime;
	}

	/**
	 * Generates a list of exercises for a training session based on the provided request, set of exercises, and user.
	 *
	 * @param  request    the request for the training session
	 * @param  exercises  the set of available exercises
	 * @param  user       the user for whom the training session is generated
	 * @return            the list of exercises for the training session
	 */
	private List<Exercise> getTrainingSessionExercises(PreviewTrainingSessionRequest request, Set<Exercise> exercises,
		User user) {
		List<Exercise> sessionExercises = new ArrayList<>();
		double usedCalories = 0;
		Exercise previousExercise = null;
		while (usedCalories <= request.getCalories()) {
			int randomIndex = ThreadLocalRandom.current().nextInt(exercises.size()); // Get a random index
			Exercise randomExercise = exercises
				.stream()
				.skip(randomIndex)
				.findFirst()
				.orElse(null); // Get the element at the random index
			if (randomExercise == null || previousExercise != null && previousExercise
				.getId()
				.equals(randomExercise.getId())) {
				continue;
			}
			sessionExercises.add(randomExercise);

			if (randomExercise.getSiblingExerciseId() != null) {
				Exercise siblingExercise = exerciseRepository
					.findById(randomExercise.getSiblingExerciseId())
					.orElse(null);
				if (siblingExercise != null) {
					usedCalories += calculateBurnedCaloriesForExercise(siblingExercise, user);
					sessionExercises.add(siblingExercise);
				}
			}
			usedCalories += calculateBurnedCaloriesForExercise(randomExercise, user);
			previousExercise = randomExercise;
		}
		return sessionExercises;
	}

	private double calculateBMR(double weight, double height, int age, String gender) {
		if (gender.equalsIgnoreCase("male")) {
			return 66.4730 + (5.0033 * height) + (13.7516 * weight) - (6.7550 * age);
		} else {
			return 655.0955 + (1.8496 * height) + (9.5634 * weight) - (4.6756 * age);
		}
	}

	private double calculateCorrectedMET(double averageMET, double weight, double height, int age, String gender) {
		double bmr = calculateBMR(weight, height, age, gender);
		double bmrMlKgMin = bmr / 1440 / 5 / weight * 1000;
		return averageMET * 3.5 / bmrMlKgMin;
	}

	private double getBurnedCaloriesForExercise(Exercise exercise, double weight, double height, int age,
		String gender) {
		double correctedMET = calculateCorrectedMET(exercise.getMET(), weight, height, age, gender);
		return correctedMET * 3.5 * weight / 200 * exercise.getDuration() * exercise.getRepetitions() / 60;
	}

	private double calculateBurnedCaloriesForExercise(Exercise exercise, User user) {
		double correctedMET = calculateCorrectedMET(exercise.getMET(), user.getWeight(), user.getHeight(),
			user.getAge(), user.getSex());
		return correctedMET * 3.5 * user.getWeight() / 200 * exercise.getDuration() * exercise.getRepetitions() / 60;
	}

	private double calculateCaloriesForExercises(List<Exercise> exercises, User user) {
		double totalCalories = 0;
		for (Exercise exercise : exercises) {
			totalCalories += calculateBurnedCaloriesForExercise(exercise, user);
		}
		return totalCalories;
	}

	private void validatePreviewTrainingSessionRequest(PreviewTrainingSessionRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Training session request cannot be null");
		}

		if (request.getCalories() != null) {
			if (request.getCalories() < 0) {
				throw new IllegalArgumentException("Calories cannot be negative");
			}
		} else {
			throw new IllegalArgumentException("Calories must be set");
		}

		if (request.getMuscleGroupId() != null) {
			if (request.getMuscleGroupId() < 0) {
				throw new IllegalArgumentException("Muscle group ID cannot be negative");
			}
		} else {
			throw new IllegalArgumentException("Muscle group ID must be set");
		}
	}
}
