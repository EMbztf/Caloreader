package com.example.dockertest.controller;

import com.example.dockertest.model.dao.MuscleGroup;
import com.example.dockertest.repository.MuscleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MuscleGroupController {
    @Autowired
    MuscleGroupRepository muscleGroupRepository;

    @GetMapping("/muscleGroups")
    public ResponseEntity<List<MuscleGroup>> getAllExercises() {
        try {
           List<MuscleGroup> muscleGroups = muscleGroupRepository.findAll();

            return new ResponseEntity<>(muscleGroups, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
