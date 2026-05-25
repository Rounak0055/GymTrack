package com.gym.backend.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDTO {
    private Long id;           // ✅ Added - needed for delete
    private String day;
    private String userName;
    private List<WorkoutEntryDTO> entries;
}
