package com.gym.backend.controller;

import com.gym.backend.entity.WorkoutEntry;
import com.gym.backend.service.WorkoutEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
public class WorkoutEntryController {

    @Autowired
    private WorkoutEntryService workoutEntryService;

    @PostMapping("/add")
    public WorkoutEntry addEntry(@RequestBody WorkoutEntry entry) {
        return workoutEntryService.addEntry(entry);
    }
}