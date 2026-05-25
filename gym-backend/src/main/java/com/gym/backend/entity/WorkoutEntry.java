package com.gym.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sets;
    private int reps;
    private double weight;

    // 🔹 Many entries belong to ONE workout
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    // 🔹 Many entries belong to ONE exercise
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}