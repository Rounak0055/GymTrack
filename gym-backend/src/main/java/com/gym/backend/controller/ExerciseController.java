package com.gym.backend.controller;

import com.gym.backend.entity.Exercise;
import com.gym.backend.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exercise")
@CrossOrigin(origins = "*")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("/add")
    public ResponseEntity<?> addExercise(@RequestBody Exercise exercise) {
        try {
            return ResponseEntity.ok(exerciseService.addExercise(exercise));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public List<Exercise> getAll() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/muscle/{type}")
    public List<Exercise> getByMuscle(@PathVariable String type) {
        return exerciseService.getByMuscle(type);
    }
}
