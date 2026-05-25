package com.gym.backend.controller;

import com.gym.backend.dto.WorkoutPlanDTO;
import com.gym.backend.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = "*")
public class PlanController {

    @Autowired
    private PlanService planService;

    // ✅ Create or update weekly plan
    @PostMapping("/save")
    public ResponseEntity<?> savePlan(@RequestBody WorkoutPlanDTO dto) {
        try {
            WorkoutPlanDTO saved = planService.savePlan(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get weekly plans for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlans(@PathVariable Long userId) {
        try {
            List<WorkoutPlanDTO> plans = planService.getPlansByUserId(userId);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get plan for specific day of week
    @GetMapping("/user/{userId}/day/{dayOfWeek}")
    public ResponseEntity<?> getPlanForDay(@PathVariable Long userId, @PathVariable String dayOfWeek) {
        try {
            return planService.getPlanForDay(userId, dayOfWeek)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
