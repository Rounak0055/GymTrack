package com.gym.backend.dto;

import lombok.Data;

@Data
public class CreateWorkoutEntryDTO {

    private Long exerciseId;
    private int sets;
    private int reps;
    private double weight;
}