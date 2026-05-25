package com.gym.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDTO {
    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;
}
