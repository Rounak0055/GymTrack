package com.gym.backend;

import com.gym.backend.entity.Exercise;
import com.gym.backend.repository.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    public DataSeeder(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {
        if (exerciseRepository.count() == 0) {
            List<Exercise> defaultExercises = new ArrayList<>();

            // Base staples
            addExercises(defaultExercises, "Chest",
                    "Barbell Bench Press");
            addExercises(defaultExercises, "Back",
                    "Wide-Grip Lat Pulldown");
            addExercises(defaultExercises, "Legs",
                    "Barbell Back Squat");
            addExercises(defaultExercises, "Shoulders",
                    "Overhead Barbell Press");
            addExercises(defaultExercises, "Arms",
                    "Dumbbell Hammer Curl");

            // 1. CHEST (additional variations)
            addExercises(defaultExercises, "Chest",
                    "Incline Dumbbell Flyes",
                    "Cable Flyes (Low-to-High)",
                    "Hammer Strength Chest Press",
                    "Landmine Press",
                    "Decline Dumbbell Press",
                    "Floor Press (Barbell)",
                    "Deficit Push-ups",
                    "Svend Press");

            // 2. BACK (additional variations)
            addExercises(defaultExercises, "Back",
                    "Rack Pulls",
                    "Meadows Row",
                    "Lat Pulldown (Close Grip V-Bar)",
                    "Lat Pulldown (Underhand Grip)",
                    "Straight-Arm Cable Pulldown",
                    "Dumbbell Pullover",
                    "Chest-Supported Dumbbell Row",
                    "Inverted Rows",
                    "Renegade Rows");

            // 3. LEGS & LOWER BODY (additional variations)
            addExercises(defaultExercises, "Legs",
                    "Hack Squat",
                    "Leg Press (Single Leg)",
                    "Sumo Deadlift",
                    "Zercher Squat",
                    "Split Squats (Dumbbell)",
                    "Hip Thrusts (Barbell)",
                    "Cable Pull-Throughs",
                    "Step-ups (Weighted)",
                    "Seated Hamstring Curl");

            // 4. SHOULDERS & TRAPS (additional variations)
            addExercises(defaultExercises, "Shoulders",
                    "Seated Barbell Overhead Press",
                    "Dumbbell Incline Rear Delt Row",
                    "Face Pulls with Rope",
                    "Upright Rows (Barbell or Cable)",
                    "Behind-the-Back Shrugs",
                    "Lu Raises",
                    "Y-Raises");

            // 5. BICEPS (additional variations)
            addExercises(defaultExercises, "Biceps",
                    "Concentration Curls",
                    "Cable Bicep Curls (Straight Bar)",
                    "Cable Bicep Curls (Rope Dual)",
                    "Spider Curls",
                    "Zottman Curls",
                    "Drag Curls",
                    "Plate Curls");

            // 6. TRICEPS (additional variations)
            addExercises(defaultExercises, "Triceps",
                    "Bench Dips (Weighted)",
                    "Triceps Kickbacks (Dumbbell)",
                    "Cable Overhead Triceps Extension (Rope)",
                    "Single-Arm Cable Pushdown",
                    "Diamond Push-ups");

            // 7. CORE, FOREARMS & CARDIO / FUNCTIONAL
            addExercises(defaultExercises, "Core",
                    "Hanging Knee Raises",
                    "Decline Sit-ups (Weighted)",
                    "Dragon Flags");
            addExercises(defaultExercises, "Forearms",
                    "Wrist Curls (Barbell)",
                    "Reverse Wrist Curls");
            addExercises(defaultExercises, "Cardio",
                    "Farmer's Walk",
                    "Kettlebell Swings",
                    "Medicine Ball Slams",
                    "Burpees");

            exerciseRepository.saveAll(defaultExercises);
            System.out.println("Database seeded with " + defaultExercises.size() + " exercises.");
        }
    }

    private void addExercises(List<Exercise> target, String muscleGroup, String... names) {
        for (String name : names) {
            target.add(createExerciseInstance(name, muscleGroup));
        }
    }

    private Exercise createExerciseInstance(String name, String muscleGroup) {
        return new Exercise(name, muscleGroup);
    }
}
