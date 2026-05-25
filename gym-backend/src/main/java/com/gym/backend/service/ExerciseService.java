package com.gym.backend.service;

import com.gym.backend.entity.Exercise;
import com.gym.backend.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    public Exercise addExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public List<Exercise> getByMuscle(String muscle) {
        return exerciseRepository.findByMuscleGroup(muscle);
    }
}