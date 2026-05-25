import { useState, useEffect } from "react";
import API from "../services/api";

function CreateWorkout({ userId, onWorkoutCreated, onCancel }) {
  const [day, setDay] = useState("");
  const [exercises, setExercises] = useState([]);
  const [selectedExercises, setSelectedExercises] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [exercisesLoading, setExercisesLoading] = useState(true);

  // ✅ Fetch exercises on mount
  useEffect(() => {
    fetchExercises();
  }, []);

  const fetchExercises = async () => {
    try {
      const res = await API.get("/exercise/all");
      setExercises(res.data);
      setExercisesLoading(false);
    } catch (err) {
      setError("Failed to load exercises");
      setExercisesLoading(false);
      console.error(err);
    }
  };

  const addExerciseEntry = () => {
    setSelectedExercises([
      ...selectedExercises,
      { exerciseId: "", sets: 3, reps: 10, weight: 0 },
    ]);
  };

  const updateExerciseEntry = (idx, field, value) => {
    const updated = [...selectedExercises];
    updated[idx][field] = value;
    setSelectedExercises(updated);
  };

  const removeExerciseEntry = (idx) => {
    setSelectedExercises(selectedExercises.filter((_, i) => i !== idx));
  };

  const handleCreateWorkout = async (e) => {
    e.preventDefault();
    setError("");

    // ✅ Validation
    if (!day.trim()) {
      setError("Workout day/name is required");
      return;
    }

    if (selectedExercises.length === 0) {
      setError("Add at least one exercise");
      return;
    }

    // ✅ Validate all exercises are selected
    if (selectedExercises.some((ex) => !ex.exerciseId)) {
      setError("All exercises must be selected");
      return;
    }

    setLoading(true);

    try {
      const payload = {
        userId: parseInt(userId),
        day,
        entries: selectedExercises.map((ex) => ({
          exerciseId: parseInt(ex.exerciseId),
          sets: parseInt(ex.sets),
          reps: parseInt(ex.reps),
          weight: parseFloat(ex.weight),
        })),
      };

      await API.post("/workout/create", payload);

      // ✅ Success - reset form and notify parent
      onWorkoutCreated();
    } catch (err) {
      if (err.response?.data?.error) {
        setError(err.response.data.error);
      } else {
        setError("Failed to create workout");
      }
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-workout-form">
      <h3>Create New Workout</h3>

      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleCreateWorkout}>
        <div className="form-group">
          <label>Workout Day/Name</label>
          <input
            type="text"
            placeholder="e.g., Chest Day, Leg Day, Monday"
            value={day}
            onChange={(e) => setDay(e.target.value)}
            disabled={loading}
          />
        </div>

        {/* ✅ Exercises Section */}
        <div className="exercises-section">
          <h4>Exercises</h4>

          {exercisesLoading ? (
            <p>Loading exercises...</p>
          ) : exercises.length === 0 ? (
            <p className="warning">
              No exercises available. Please add exercises first.
            </p>
          ) : (
            <>
              {selectedExercises.map((entry, idx) => (
                <div key={idx} className="exercise-entry">
                  <div className="exercise-select">
                    <select
                      value={entry.exerciseId}
                      onChange={(e) =>
                        updateExerciseEntry(idx, "exerciseId", e.target.value)
                      }
                      disabled={loading}
                    >
                      <option value="">Select Exercise</option>
                      {exercises.map((ex) => (
                        <option key={ex.id} value={ex.id}>
                          {ex.name} ({ex.muscleGroup})
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="exercise-details">
                    <input
                      type="number"
                      placeholder="Sets"
                      min="1"
                      value={entry.sets}
                      onChange={(e) =>
                        updateExerciseEntry(idx, "sets", e.target.value)
                      }
                      disabled={loading}
                    />
                    <input
                      type="number"
                      placeholder="Reps"
                      min="1"
                      value={entry.reps}
                      onChange={(e) =>
                        updateExerciseEntry(idx, "reps", e.target.value)
                      }
                      disabled={loading}
                    />
                    <input
                      type="number"
                      placeholder="Weight (kg)"
                      step="0.5"
                      min="0"
                      value={entry.weight}
                      onChange={(e) =>
                        updateExerciseEntry(idx, "weight", e.target.value)
                      }
                      disabled={loading}
                    />
                  </div>

                  <button
                    type="button"
                    onClick={() => removeExerciseEntry(idx)}
                    className="btn-remove"
                    disabled={loading}
                  >
                    Remove
                  </button>
                </div>
              ))}

              <button
                type="button"
                onClick={addExerciseEntry}
                className="btn-add-exercise"
                disabled={loading}
              >
                + Add Exercise
              </button>
            </>
          )}
        </div>

        {/* ✅ Action Buttons */}
        <div className="form-actions">
          <button
            type="submit"
            disabled={loading || exercisesLoading}
            className="btn-primary"
          >
            {loading ? "Creating..." : "Create Workout"}
          </button>
          <button
            type="button"
            onClick={onCancel}
            disabled={loading}
            className="btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

export default CreateWorkout;
