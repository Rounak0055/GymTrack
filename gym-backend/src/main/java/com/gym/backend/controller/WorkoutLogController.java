package com.gym.backend.controller;

import com.gym.backend.dto.WorkoutLogDTO;
import com.gym.backend.dto.WorkoutPlanDTO;
import com.gym.backend.service.PlanService;
import com.gym.backend.service.WorkoutLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/log")
@CrossOrigin(origins = "*")
public class WorkoutLogController {

    @Autowired
    private WorkoutLogService logService;

    @Autowired
    private PlanService planService;

    // ✅ Save or update a daily workout log
    @PostMapping("/save")
    public ResponseEntity<?> saveLog(@RequestBody WorkoutLogDTO dto) {
        try {
            WorkoutLogDTO saved = logService.saveLog(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get workout history for a user
    @GetMapping("/user/{userId}/history")
    public ResponseEntity<?> getHistory(@PathVariable Long userId) {
        try {
            List<WorkoutLogDTO> history = logService.getHistoryByUserId(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Delete a workout log
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable Long id) {
        try {
            logService.deleteLog(id);
            return ResponseEntity.ok(Map.of("message", "Workout log deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get today's logged workout, or today's plan template if not logged yet
    // NOTE: Uses HashMap (not Map.of) because Map.of() does NOT allow null values
    @GetMapping("/user/{userId}/today")
    public ResponseEntity<?> getTodayWorkout(@PathVariable Long userId) {
        try {
            LocalDate today = LocalDate.now();
            Optional<WorkoutLogDTO> logOpt = logService.getLogForDate(userId, today);

            if (logOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("isLogged", true);
                response.put("log", logOpt.get());
                response.put("dayOfWeek", today.getDayOfWeek().toString());
                return ResponseEntity.ok(response);
            } else {
                String dayOfWeek = today.getDayOfWeek().toString();
                Optional<WorkoutPlanDTO> planOpt = planService.getPlanForDay(userId, dayOfWeek);

                Map<String, Object> response = new HashMap<>();
                response.put("isLogged", false);
                response.put("dayOfWeek", dayOfWeek);
                // HashMap allows null — planOpt.orElse(null) is safe here
                response.put("template", planOpt.orElse(null));
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
