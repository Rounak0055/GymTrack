package com.gym.backend.service;

import com.gym.backend.dto.WorkoutLogDTO;
import com.gym.backend.dto.WorkoutLogEntryDTO;
import com.gym.backend.entity.*;
import com.gym.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutLogService {

    @Autowired
    private WorkoutLogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutPlanRepository planRepository;

    // ✅ Save or update a daily workout log
    public WorkoutLogDTO saveLog(WorkoutLogDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        LocalDate logDate = LocalDate.parse(dto.getDate());

        Optional<WorkoutLog> existingLogOpt = logRepository.findByUserIdAndDate(dto.getUserId(), logDate);
        WorkoutLog log;

        if (existingLogOpt.isPresent()) {
            log = existingLogOpt.get();
            log.getEntries().clear(); // Triggers orphan removal
        } else {
            log = new WorkoutLog();
            log.setUser(user);
            log.setDate(logDate);
            log.setEntries(new ArrayList<>());
        }

        if (dto.getWorkoutPlanId() != null) {
            WorkoutPlan plan = planRepository.findById(dto.getWorkoutPlanId())
                    .orElse(null);
            log.setWorkoutPlan(plan);
        } else {
            log.setWorkoutPlan(null);
        }

        WorkoutLog savedLog = logRepository.save(log);

        // Map and save entries
        if (dto.getEntries() != null) {
            for (WorkoutLogEntryDTO entryDTO : dto.getEntries()) {
                Exercise exercise = exerciseRepository.findById(entryDTO.getExerciseId())
                        .orElseThrow(() -> new RuntimeException("Exercise not found: " + entryDTO.getExerciseId()));

                WorkoutLogEntry entry = new WorkoutLogEntry();
                entry.setWorkoutLog(savedLog);
                entry.setExercise(exercise);
                entry.setActualSets(entryDTO.getActualSets());
                entry.setActualReps(entryDTO.getActualReps());
                entry.setWeight(entryDTO.getWeight());

                savedLog.getEntries().add(entry);
            }
        }

        WorkoutLog finalLog = logRepository.save(savedLog);
        return convertToDTO(finalLog);
    }

    // ✅ Get all history for a user
    public List<WorkoutLogDTO> getHistoryByUserId(Long userId) {
        return logRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get log for a specific date
    public Optional<WorkoutLogDTO> getLogForDate(Long userId, LocalDate date) {
        return logRepository.findByUserIdAndDate(userId, date)
                .map(this::convertToDTO);
    }

    // ✅ Delete a workout log (entire day)
    public void deleteLog(Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new RuntimeException("Workout log not found with id: " + logId);
        }
        logRepository.deleteById(logId);
    }

    // ✅ Helper method to convert Entity to DTO
    private WorkoutLogDTO convertToDTO(WorkoutLog log) {
        WorkoutLogDTO dto = new WorkoutLogDTO();
        dto.setId(log.getId());
        dto.setUserId(log.getUser().getId());
        dto.setDate(log.getDate().toString());
        dto.setWorkoutPlanId(log.getWorkoutPlan() != null ? log.getWorkoutPlan().getId() : null);
        dto.setWorkoutPlanName(log.getWorkoutPlan() != null ? log.getWorkoutPlan().getName() : "Unplanned");

        List<WorkoutLogEntryDTO> entryDTOs = new ArrayList<>();
        if (log.getEntries() != null) {
            for (WorkoutLogEntry entry : log.getEntries()) {
                WorkoutLogEntryDTO entryDTO = new WorkoutLogEntryDTO(
                        entry.getId(),
                        entry.getExercise().getId(),
                        entry.getExercise().getName(),
                        entry.getExercise().getMuscleGroup(),
                        entry.getActualSets(),
                        entry.getActualReps(),
                        entry.getWeight()
                );
                entryDTOs.add(entryDTO);
            }
        }
        dto.setEntries(entryDTOs);
        return dto;
    }
}
