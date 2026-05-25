package com.gym.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogEntryDTO {
    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;
    private int actualSets;
    private int actualReps;
    private double weight;
}
