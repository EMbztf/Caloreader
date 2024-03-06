package com.example.dockertest.controller;

import com.example.dockertest.model.dao.Exercise;
import com.example.dockertest.model.dao.MuscleGroup;
import com.example.dockertest.model.dao.Stretch;
import com.example.dockertest.model.dao.Warmup;
import com.example.dockertest.model.request.TrainingSessionRequest;
import com.example.dockertest.model.response.TrainingSessionResponse;
import com.example.dockertest.repository.ExerciseRepository;
import com.example.dockertest.repository.MuscleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
public class ExerciseController {
    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    MuscleGroupRepository muscleGroupRepository;

    @PostMapping("/test")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }

    @PostMapping("/previewTrainingSessions")
    public ResponseEntity<TrainingSessionResponse> postPreviewTrainingSessions(@RequestBody TrainingSessionRequest request) {
        try {
            validateTrainingSessionRequest(request);
            Optional<MuscleGroup> muscleGroup = muscleGroupRepository.findById(request.getMuscleGroupId());
            TrainingSessionResponse response = null;
            if(muscleGroup.isPresent()) {
                Set<Exercise> exercises = muscleGroup.get().getExercises();
                if (exercises.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }

                List<Warmup> warmups = muscleGroup.get().getWarmups().stream().toList();
                List<Stretch> stretches = muscleGroup.get().getStretches().stream().toList();

                double defaultCalories = calculateCaloriesForWarmupsAndStretches(warmups, stretches, request);
                request.setCalories((int) (request.getCalories() - defaultCalories));

                List<Exercise> sessionExercises = getTrainingSessionExercises(request, exercises);
                double estimatedTime = calculateEstimatedTime(warmups, sessionExercises, stretches);

                response = new TrainingSessionResponse(estimatedTime, sessionExercises, warmups, stretches);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private double calculateEstimatedTime(List<Warmup> warmups, List<Exercise> exercises, List<Stretch> stretches) {
        double estimatedTime = 0;
        double restTime = 30;
        double stretchRestTime = 10;

        for(Warmup warmup : warmups) {
            estimatedTime += warmup.getDuration() * warmup.getRepetitions();
        }

        for(Exercise exercise : exercises) {
            estimatedTime += exercise.getDuration() * exercise.getRepetitions();
        }

        for (Stretch stretch : stretches) {
            estimatedTime += stretch.getDuration() * stretch.getRepetitions();
        }

        estimatedTime += warmups.size() * restTime;
        estimatedTime += exercises.size() * restTime;
        estimatedTime += (stretches.size() -1) * stretchRestTime;

        return estimatedTime;
    }

    private List<Exercise> getTrainingSessionExercises(TrainingSessionRequest request, Set<Exercise> exercises) {
        List<Exercise> sessionExercises = new ArrayList<>();
        double usedCalories = 0;
        Exercise previousExercise = null;
        while (usedCalories <= request.getCalories()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(exercises.size()); // Get a random index
            Exercise randomExercise = exercises.stream()
              .skip(randomIndex)
              .findFirst()
              .orElse(null); // Get the element at the random index
            if(randomExercise == null || previousExercise != null && previousExercise.getId().equals(randomExercise.getId())) {
                continue;
            }
            sessionExercises.add(randomExercise);

            if(randomExercise.getSiblingExerciseId() != null) {
                Exercise siblingExercise = exerciseRepository.findById(randomExercise.getSiblingExerciseId()).orElse(null);
                if(siblingExercise != null) {
                    usedCalories += calculateBurnedCalories(siblingExercise.getMET(), siblingExercise.getDuration(), siblingExercise.getRepetitions(),request);
                    sessionExercises.add(siblingExercise);
                }
            }
            usedCalories += calculateBurnedCalories(randomExercise.getMET(), randomExercise.getDuration(), randomExercise.getRepetitions(), request);
            previousExercise = randomExercise;
        }
        return sessionExercises;
    }

    private double calculateBMR(double weight, int height, int age, String gender) {
        if(gender.equalsIgnoreCase("male")) {
            return 66.4730 + (5.0033 * height) + (13.7516 * weight) - (6.7550 * age);
        } else {
            return 655.0955 + (1.8496 * height) + (9.5634 * weight) - (4.6756 * age);
        }
    }

    private double calculateCorrectedMET(double averageMET, double weight, int height, int age, String gender) {
        double bmr = calculateBMR(weight, height, age, gender);
        double bmrMlKgMin = bmr / 1440 / 5 / weight * 1000;
        return averageMET * 3.5 / bmrMlKgMin;
    }

    private double getBurnedCaloriesForExercise(Exercise exercise, double weight, int height, int age, String gender) {
        double correctedMET = calculateCorrectedMET(exercise.getMET(), weight, height, age, gender);
        return correctedMET * 3.5 * weight / 200 * exercise.getDuration() * exercise.getRepetitions() / 60;
    }

    private double calculateBurnedCalories(double met, double duration, int repetitions, TrainingSessionRequest request) {
        double correctedMET = calculateCorrectedMET(met, request.getWeight(), request.getHeight(), request.getAge(), request.getGender());
        return correctedMET * 3.5 * request.getWeight() / 200 * duration * repetitions / 60;
    }

    private double calculateCaloriesForWarmupsAndStretches(List<Warmup> warmups, List<Stretch> stretches, TrainingSessionRequest request) {
        double totalCalories = 0;
        for(Warmup warmup : warmups) {
            totalCalories += calculateBurnedCalories(warmup.getMET(), warmup.getDuration(), warmup.getRepetitions(), request);
        }
        for(Stretch stretch : stretches) {
            totalCalories += calculateBurnedCalories(stretch.getMET(), stretch.getDuration(), stretch.getRepetitions(), request);
        }
        return totalCalories;
    }

    private void validateTrainingSessionRequest(TrainingSessionRequest request) {
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

        if (request.getWeight() != null) {
            if (request.getWeight() < 0) {
                throw new IllegalArgumentException("Weight cannot be negative");
            }
        } else {
            throw new IllegalArgumentException("Weight must be set");
        }

        if (request.getHeight() != null) {
            if (request.getHeight() < 0) {
                throw new IllegalArgumentException("Height cannot be negative");
            }
        } else {
            throw new IllegalArgumentException("Height must be set");
        }

        if (request.getAge() != null) {
            if (request.getAge() < 0) {
                throw new IllegalArgumentException("Age must be greater than or equal to 0");
            }
        } else {
            throw new IllegalArgumentException("Age must be set");
        }

        if (request.getGender() == null || request.getGender().isEmpty()) {
            throw new IllegalArgumentException("Gender must be set");
        }
    }
}
