package com.gym.backend.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek; // MONDAY, TUESDAY, etc.
    private String name;      // Plan name, e.g. "Chest Day"

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> exercises;
}
