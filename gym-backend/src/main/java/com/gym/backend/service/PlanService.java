package com.gym.backend.service;

import com.gym.backend.dto.WorkoutExerciseDTO;
import com.gym.backend.dto.WorkoutPlanDTO;
import com.gym.backend.entity.Exercise;
import com.gym.backend.entity.User;
import com.gym.backend.entity.WorkoutExercise;
import com.gym.backend.entity.WorkoutPlan;
import com.gym.backend.repository.ExerciseRepository;
import com.gym.backend.repository.UserRepository;
import com.gym.backend.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanService {

    @Autowired
    private WorkoutPlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    // ✅ Save or update a weekly workout plan
    public WorkoutPlanDTO savePlan(WorkoutPlanDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Optional<WorkoutPlan> existingPlanOpt = planRepository.findByUserIdAndDayOfWeek(dto.getUserId(), dto.getDayOfWeek());
        WorkoutPlan plan;

        if (existingPlanOpt.isPresent()) {
            plan = existingPlanOpt.get();
            plan.setName(dto.getName());
            plan.getExercises().clear(); // Triggers orphan removal
        } else {
            plan = new WorkoutPlan();
            plan.setUser(user);
            plan.setDayOfWeek(dto.getDayOfWeek());
            plan.setName(dto.getName());
            plan.setExercises(new ArrayList<>());
        }

        WorkoutPlan savedPlan = planRepository.save(plan);

        // Map and save new exercises
        if (dto.getExercises() != null) {
            for (WorkoutExerciseDTO weDTO : dto.getExercises()) {
                Exercise exercise = exerciseRepository.findById(weDTO.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found: " + weDTO.getExerciseId()));

                WorkoutExercise workoutExercise = new WorkoutExercise();
                workoutExercise.setWorkoutPlan(savedPlan);
                workoutExercise.setExercise(exercise);

                savedPlan.getExercises().add(workoutExercise);
            }
        }

        // Save again to persist child relations
        WorkoutPlan finalPlan = planRepository.save(savedPlan);
        return convertToDTO(finalPlan);
    }

    // ✅ Get weekly plans for a specific user
    public List<WorkoutPlanDTO> getPlansByUserId(Long userId) {
        return planRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get plan for a specific day of week
    public Optional<WorkoutPlanDTO> getPlanForDay(Long userId, String dayOfWeek) {
        return planRepository.findByUserIdAndDayOfWeek(userId, dayOfWeek.toUpperCase())
                .map(this::convertToDTO);
    }

    // ✅ Helper method to convert Entity to DTO
    private WorkoutPlanDTO convertToDTO(WorkoutPlan plan) {
        WorkoutPlanDTO dto = new WorkoutPlanDTO();
        dto.setId(plan.getId());
        dto.setUserId(plan.getUser().getId());
        dto.setDayOfWeek(plan.getDayOfWeek());
        dto.setName(plan.getName());

        List<WorkoutExerciseDTO> exerciseDTOs = new ArrayList<>();
        if (plan.getExercises() != null) {
            for (WorkoutExercise we : plan.getExercises()) {
                WorkoutExerciseDTO weDTO = new WorkoutExerciseDTO(
                        we.getId(),
                        we.getExercise().getId(),
                        we.getExercise().getName(),
                        we.getExercise().getMuscleGroup()
                );
                exerciseDTOs.add(weDTO);
            }
        }
        dto.setExercises(exerciseDTOs);
        return dto;
    }
}
