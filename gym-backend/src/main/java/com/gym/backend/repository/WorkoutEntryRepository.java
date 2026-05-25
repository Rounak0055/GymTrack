package com.gym.backend.repository;

import com.gym.backend.entity.WorkoutEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutEntryRepository extends JpaRepository<WorkoutEntry, Long> {
}