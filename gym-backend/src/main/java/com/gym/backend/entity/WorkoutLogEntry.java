package com.gym.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_log_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_log_id")
    @JsonIgnore
    private WorkoutLog workoutLog;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int actualSets;
    private int actualReps;
    private double weight;
}
