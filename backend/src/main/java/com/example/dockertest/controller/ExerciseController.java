package com.example.dockertest.controller;

import com.example.dockertest.model.Exercise;
import com.example.dockertest.model.MuscleGroup;
import com.example.dockertest.model.TrainingSessionRequest;
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

    @GetMapping("/trainingSession")
    public ResponseEntity<List<Exercise>> getAllExercises(@RequestBody TrainingSessionRequest request) {
        try {
            validateTrainingSessionRequest(request);
            Optional<MuscleGroup> muscleGroup = muscleGroupRepository.findById(request.getMuscleGroupId());
            List<Exercise> sessionExercises = new ArrayList<>();
            if(muscleGroup.isPresent()) {
                Set<Exercise> exercises = muscleGroup.get().getExercises();
                if (exercises.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }

                double usedCalories = 0;
                while (usedCalories <= request.getCalories()) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(exercises.size()); // Get a random index
                    Exercise randomExercise = exercises.stream()
                      .skip(randomIndex)
                      .findFirst()
                      .orElse(null); // Get the element at the random index
                    if(randomExercise == null) {
                        break;
                    }
                    if(randomExercise.isBothSides()) {
                        Exercise leftExercise = new Exercise(randomExercise.getName() + " left", randomExercise.getMET(), randomExercise.getDuration(), randomExercise.getRepetitions(), randomExercise.getVideoPath(), randomExercise.isBothSides());
                        Exercise rightExercise = new Exercise(randomExercise.getName() + " right", randomExercise.getMET(), randomExercise.getDuration(), randomExercise.getRepetitions(), randomExercise.getVideoPath(), randomExercise.isBothSides());
                        sessionExercises.add(leftExercise);
                        sessionExercises.add(rightExercise);
                    } else {
                        sessionExercises.add(randomExercise);
                    }

                    for(Exercise exercise : sessionExercises) {
                        usedCalories += getBurnedCaloriesForExercise(exercise, request.getWeight(), request.getHeight(), request.getAge(), request.getGender());
                    }
               }

                System.out.println("Used calories: " + usedCalories);
            }

            return new ResponseEntity<>(sessionExercises, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/exercise/{id}")
//    public ResponseEntity<Exercise> getTutorialById(@PathVariable("id") Integer id) {
//        Optional<Exercise> exerciseData = exerciseRepository.findById(id);
//
//        if (exerciseData.isPresent()) {
//            return new ResponseEntity<>(exerciseData.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping(value = "/exercises")
//    public ResponseEntity<Exercise> createTutorial(@RequestBody Exercise exercise) {
//        try {
//            Exercise newExercise = exerciseRepository
//              .save(new Exercise(exercise.getName(), exercise.getDuration(), exercise.getRepetitions(), exercise.getVideoPath()));
//            return new ResponseEntity<>(newExercise, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

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
