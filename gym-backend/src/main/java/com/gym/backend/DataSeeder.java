package com.gym.backend;

import com.gym.backend.entity.Exercise;
import com.gym.backend.entity.User;
import com.gym.backend.entity.WorkoutExercise;
import com.gym.backend.entity.WorkoutPlan;
import com.gym.backend.repository.ExerciseRepository;
import com.gym.backend.repository.UserRepository;
import com.gym.backend.repository.WorkoutLogRepository;
import com.gym.backend.repository.WorkoutPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final String SYSTEM_USER_EMAIL = "templates@gymtrack.local";

    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final UserRepository userRepository;

    public DataSeeder(
            ExerciseRepository exerciseRepository,
            WorkoutPlanRepository workoutPlanRepository,
            WorkoutLogRepository workoutLogRepository,
            UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutLogRepository = workoutLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // workoutLogRepository.deleteAll();
        // workoutPlanRepository.deleteAll();
        // exerciseRepository.deleteAll();

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

            // Template staples (names used by default routines)
            addExercises(defaultExercises, "Chest",
                    "Incline Barbell Press");
            addExercises(defaultExercises, "Shoulders",
                    "Lateral Raises");
            addExercises(defaultExercises, "Back",
                    "Barbell Bent-Over Row");
            addExercises(defaultExercises, "Legs",
                    "Romanian Deadlift",
                    "Bulgarian Split Squat",
                    "Standing Calf Raises");
            addExercises(defaultExercises, "Core",
                    "Plank");

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

        if (workoutPlanRepository.count() == 0) {
            Map<String, Exercise> exercisesByName = indexExercisesByName();
            User systemUser = getOrCreateSystemUser();

            List<TemplateDefinition> defaultTemplates = List.of(
                    new TemplateDefinition(
                            "Push Day",
                            "TEMPLATE_PUSH",
                            "Barbell Bench Press",
                            "Overhead Barbell Press",
                            "Incline Barbell Press",
                            "Lateral Raises",
                            "Cable Overhead Triceps Extension (Rope)"),
                    new TemplateDefinition(
                            "Pull Day",
                            "TEMPLATE_PULL",
                            "Wide-Grip Lat Pulldown",
                            "Barbell Bent-Over Row",
                            "Face Pulls with Rope",
                            "Concentration Curls",
                            "Dumbbell Hammer Curl"),
                    new TemplateDefinition(
                            "Leg Day",
                            "TEMPLATE_LEG",
                            "Barbell Back Squat",
                            "Romanian Deadlift",
                            "Bulgarian Split Squat",
                            "Seated Hamstring Curl",
                            "Standing Calf Raises"),
                    new TemplateDefinition(
                            "Full Body Split",
                            "TEMPLATE_FULL_BODY",
                            "Barbell Back Squat",
                            "Barbell Bench Press",
                            "Wide-Grip Lat Pulldown",
                            "Overhead Barbell Press",
                            "Plank")
            );

            for (TemplateDefinition template : defaultTemplates) {
                workoutPlanRepository.save(buildWorkoutPlan(systemUser, template, exercisesByName));
            }

            cloneDefaultTemplatesToAllUsers(systemUser, defaultTemplates, exercisesByName);

            System.out.println("Database seeded with " + defaultTemplates.size()
                    + " default routine templates per user.");
        }
    }

    private void cloneDefaultTemplatesToAllUsers(
            User systemUser,
            List<TemplateDefinition> templates,
            Map<String, Exercise> exercisesByName) {
        for (User user : userRepository.findAll()) {
            if (SYSTEM_USER_EMAIL.equals(user.getEmail())) {
                continue;
            }
            for (TemplateDefinition template : templates) {
                workoutPlanRepository.save(buildWorkoutPlan(user, template, exercisesByName));
            }
        }
    }

    private User getOrCreateSystemUser() {
        User existing = userRepository.findByEmail(SYSTEM_USER_EMAIL);
        if (existing != null) {
            return existing;
        }
        User systemUser = new User();
        systemUser.setName("GymTrack Templates");
        systemUser.setEmail(SYSTEM_USER_EMAIL);
        systemUser.setPassword("N/A");
        return userRepository.save(systemUser);
    }

    private Map<String, Exercise> indexExercisesByName() {
        Map<String, Exercise> byName = new HashMap<>();
        for (Exercise exercise : exerciseRepository.findAll()) {
            byName.put(exercise.getName(), exercise);
        }
        return byName;
    }

    private WorkoutPlan buildWorkoutPlan(
            User user,
            TemplateDefinition template,
            Map<String, Exercise> exercisesByName) {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setName(template.name());
        plan.setDayOfWeek(template.dayKey());
        plan.setUser(user);
        plan.setExercises(new ArrayList<>());

        for (String exerciseName : template.exerciseNames()) {
            Exercise exercise = exercisesByName.get(exerciseName);
            if (exercise == null) {
                throw new IllegalStateException(
                        "Template \"" + template.name() + "\" references missing exercise: " + exerciseName);
            }

            WorkoutExercise workoutExercise = new WorkoutExercise();
            workoutExercise.setWorkoutPlan(plan);
            workoutExercise.setExercise(exercise);
            plan.getExercises().add(workoutExercise);
        }

        return plan;
    }

    private void addExercises(List<Exercise> target, String muscleGroup, String... names) {
        for (String name : names) {
            target.add(new Exercise(name, muscleGroup));
        }
    }

    private record TemplateDefinition(String name, String dayKey, String... exerciseNames) {}
}
