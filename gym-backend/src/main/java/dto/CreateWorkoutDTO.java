package com.gym.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateWorkoutDTO {

    private Long userId;
    private String day;
    private List<CreateWorkoutEntryDTO> entries;
}