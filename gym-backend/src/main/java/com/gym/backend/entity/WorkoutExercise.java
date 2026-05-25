package com.gym.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_plan_id")
    @JsonIgnore
    private WorkoutPlan workoutPlan;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}
