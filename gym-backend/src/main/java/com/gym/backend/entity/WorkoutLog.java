package com.gym.backend.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "workout_plan_id", nullable = true)
    private WorkoutPlan workoutPlan;

    @OneToMany(mappedBy = "workoutLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutLogEntry> entries;
}
