package com.gym.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogDTO {
    private Long id;
    private Long userId;
    private String date; // Handled as YYYY-MM-DD string
    private Long workoutPlanId;
    private String workoutPlanName;
    private List<WorkoutLogEntryDTO> entries;
}
