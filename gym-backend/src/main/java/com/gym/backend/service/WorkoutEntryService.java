package com.gym.backend.service;

import com.gym.backend.entity.WorkoutEntry;
import com.gym.backend.repository.WorkoutEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkoutEntryService {

    @Autowired
    private WorkoutEntryRepository workoutEntryRepository;

    public WorkoutEntry addEntry(WorkoutEntry entry) {
        return workoutEntryRepository.save(entry);
    }
}
