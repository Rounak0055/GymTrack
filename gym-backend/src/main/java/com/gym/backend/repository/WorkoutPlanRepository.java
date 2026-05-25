package com.gym.backend.repository;

import com.gym.backend.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByUserId(Long userId);
    Optional<WorkoutPlan> findByUserIdAndDayOfWeek(Long userId, String dayOfWeek);
}
