package com.gym.backend.service;

import com.gym.backend.dto.*;
import com.gym.backend.entity.*;
import com.gym.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutEntryRepository workoutEntryRepository;

    // ✅ Create workout from DTO (with entries in same request)
    public Workout createWorkoutFromDTO(CreateWorkoutDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Workout workout = new Workout();
        workout.setDay(dto.getDay());
        workout.setUser(user);

        Workout savedWorkout = workoutRepository.save(workout);

        // Save each entry
        if (dto.getEntries() != null) {
            for (CreateWorkoutEntryDTO entryDTO : dto.getEntries()) {
                Exercise exercise = exerciseRepository.findById(entryDTO.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found: " + entryDTO.getExerciseId()));

                WorkoutEntry entry = new WorkoutEntry();
                entry.setSets(entryDTO.getSets());
                entry.setReps(entryDTO.getReps());
                entry.setWeight(entryDTO.getWeight());
                entry.setWorkout(savedWorkout);
                entry.setExercise(exercise);

                workoutEntryRepository.save(entry);
            }
        }

        return savedWorkout;
    }

    // ✅ Simple create (without entries)
    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    // ✅ Get workouts by user ID as DTOs
    public List<WorkoutDTO> getWorkoutsByUserId(Long userId) {
        List<Workout> workouts = workoutRepository.findByUserId(userId);

        return workouts.stream().map(workout -> {
            List<WorkoutEntryDTO> entryDTOs = new ArrayList<>();

            if (workout.getEntries() != null) {
                entryDTOs = workout.getEntries().stream().map(entry ->
                        new WorkoutEntryDTO(
                                entry.getSets(),
                                entry.getReps(),
                                entry.getWeight(),
                                entry.getExercise() != null ? entry.getExercise().getName() : "Unknown"
                        )
                ).toList();
            }

            return new WorkoutDTO(workout.getId(), workout.getDay(), workout.getUser().getName(), entryDTOs);
        }).toList();
    }

    // ✅ All workout details as DTOs
    public List<WorkoutDTO> getAllWorkoutDetails() {
        return getWorkoutsByUserId(null); // fallback to full list
    }

    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
}
