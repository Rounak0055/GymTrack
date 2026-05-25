package com.gym.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutEntryDTO {

    private int sets;
    private int reps;
    private double weight;
    private String exerciseName;
}