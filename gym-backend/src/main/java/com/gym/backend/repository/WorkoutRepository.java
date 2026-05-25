package com.gym.backend.repository;

import com.gym.backend.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    // ✅ Find all workouts for a specific user
    List<Workout> findByUserId(Long userId);
}
