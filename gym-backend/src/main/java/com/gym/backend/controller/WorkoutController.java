package com.gym.backend.controller;

import com.gym.backend.dto.CreateWorkoutDTO;
import com.gym.backend.dto.WorkoutDTO;
import com.gym.backend.entity.Workout;
import com.gym.backend.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workout")
@CrossOrigin(origins = "*")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    // ✅ Create workout with entries in one call
    @PostMapping("/create")
    public ResponseEntity<?> createWorkout(@RequestBody CreateWorkoutDTO dto) {
        try {
            Workout workout = workoutService.createWorkoutFromDTO(dto);
            return ResponseEntity.ok(Map.of(
                    "id", workout.getId(),
                    "day", workout.getDay(),
                    "message", "Workout created successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all workouts for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getWorkoutsByUser(@PathVariable Long userId) {
        try {
            List<WorkoutDTO> workouts = workoutService.getWorkoutsByUserId(userId);
            return ResponseEntity.ok(workouts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all workouts (admin/debug)
    @GetMapping("/all")
    public List<Workout> getAll() {
        return workoutService.getAllWorkouts();
    }

    @GetMapping("/details")
    public List<WorkoutDTO> getWorkoutDetails() {
        return workoutService.getAllWorkoutDetails();
    }

    // ✅ Delete a workout
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long id) {
        try {
            workoutService.deleteWorkout(id);
            return ResponseEntity.ok(Map.of("message", "Workout deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
