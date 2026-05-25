package com.gym.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanDTO {
    private Long id;
    private Long userId;
    private String dayOfWeek;
    private String name;
    private List<WorkoutExerciseDTO> exercises;
}
