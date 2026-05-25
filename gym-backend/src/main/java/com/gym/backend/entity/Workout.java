package com.gym.backend.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workouts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day; // Example: Monday, Chest Day

    // 🔹 Many workouts belong to ONE user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 🔹 One workout has MANY entries (exercises)
    @JsonIgnore
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutEntry> entries;
}