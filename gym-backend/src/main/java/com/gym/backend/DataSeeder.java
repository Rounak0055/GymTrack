package com.gym.backend;

import com.gym.backend.entity.Exercise;
import com.gym.backend.repository.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    public DataSeeder(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (exerciseRepository.count() == 0) {
            List<Exercise> defaultExercises = new ArrayList<>();

            defaultExercises.add(createExerciseInstance("Barbell Bench Press", "Chest"));
            defaultExercises.add(createExerciseInstance("Barbell Back Squat", "Legs"));
            defaultExercises.add(createExerciseInstance("Dumbbell Hammer Curl", "Arms"));
            defaultExercises.add(createExerciseInstance("Wide-Grip Lat Pulldown", "Back"));
            defaultExercises.add(createExerciseInstance("Overhead Barbell Press", "Shoulders"));

            exerciseRepository.saveAll(defaultExercises);
            System.out.println("🎉 Database successfully populated with base gym exercises!");
        }
    }

    // Direct helper method to bypass Lombok setter issues entirely
    private Exercise createExerciseInstance(String name, String muscleGroup) {
        try {
            Exercise exercise = Exercise.class.getDeclaredConstructor().newInstance();
            
            // Set name field directly bypassing setters
            Field nameField = Exercise.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(exercise, name);

            // Set muscleGroup field directly bypassing setters
            Field mgField = Exercise.class.getDeclaredField("muscleGroup");
            mgField.setAccessible(true);
            mgField.set(exercise, muscleGroup);

            return exercise;
        } catch (NoSuchFieldException e) {
            // Fallback check: try lowercase muscle_group naming style if camelCase isn't found
            try {
                Exercise exercise = Exercise.class.getDeclaredConstructor().newInstance();
                Field nameField = Exercise.class.getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(exercise, name);

                Field mgField = Exercise.class.getDeclaredField("muscle_group");
                mgField.setAccessible(true);
                mgField.set(exercise, muscleGroup);
                return exercise;
            } catch (Exception ex) {
                throw new RuntimeException("Could not seed data due to field naming mismatch", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed creating exercise entity record", e);
        }
    }
}