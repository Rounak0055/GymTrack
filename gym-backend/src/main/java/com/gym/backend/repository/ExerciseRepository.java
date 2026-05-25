package com.gym.backend.repository;

import com.gym.backend.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByMuscleGroup(String muscleGroup);
}