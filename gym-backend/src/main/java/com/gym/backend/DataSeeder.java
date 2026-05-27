package com.gym.backend;

import com.gym.backend.entity.Exercise;
import com.gym.backend.repository.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    public DataSeeder(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only insert if the database dropdown table is currently empty
        if (exerciseRepository.count() == 0) {
            List<Exercise> defaultExercises = Arrays.asList(
                new Exercise("Barbell Bench Press", "Chest"),
                new Exercise("Incline Dumbbell Press", "Chest"),
                new Exercise("Barbell Back Squat", "Legs"),
                new Exercise("Leg Press Machine", "Legs"),
                new Exercise("Dumbbell Hammer Curl", "Arms"),
                new Exercise("Tricep Rope Pushdown", "Arms"),
                new Exercise("Conventional Deadlift", "Back"),
                new Exercise("Wide-Grip Lat Pulldown", "Back"),
                new Exercise("Overhead Barbell Press", "Shoulders"),
                new Exercise("Dumbbell Lateral Raise", "Shoulders")
            );
            
            exerciseRepository.saveAll(defaultExercises);
            System.out.println("🎉 Database successfully populated with base gym exercises!");
        }
    }
}