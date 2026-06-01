import { useState, useEffect } from "react";
import API from "../services/api";

const MUSCLE_GROUPS = [
  "Chest", "Back", "Legs", "Shoulders", "Biceps", "Triceps", "Core", "Forearms", "Cardio"
];

function TodayWorkout({ userId, onNavigate }) {
  const [isLogged, setIsLogged] = useState(false);
  const [logId, setLogId] = useState(null);
  const [workoutPlanId, setWorkoutPlanId] = useState(null);
  const [workoutPlanName, setWorkoutPlanName] = useState("Unplanned");
  const [dayOfWeek, setDayOfWeek] = useState("");
  const [logEntries, setLogEntries] = useState([]);
  
  // Weekly plans list (in case user wants to load another day's plan)
  const [weeklyPlans, setWeeklyPlans] = useState([]);
  const [availableExercises, setAvailableExercises] = useState([]);
  
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const getLocalDateString = () => {
    const d = new Date();
    const offset = d.getTimezoneOffset();
    const localDate = new Date(d.getTime() - (offset * 60 * 1000));
    return localDate.toISOString().split('T')[0];
  };

  useEffect(() => {
    fetchAvailableExercises();
    fetchWeeklyPlans();
    fetchTodayStatus();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchAvailableExercises = async () => {
    try {
      const res = await API.get("/exercise/all");
      setAvailableExercises(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchWeeklyPlans = async () => {
    try {
      const res = await API.get(`/plan/user/${userId}`);
      setWeeklyPlans(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchTodayStatus = async () => {
    try {
      setLoading(true);
      const res = await API.get(`/log/user/${userId}/today`);
      setDayOfWeek(res.data.dayOfWeek || "");

      if (res.data.isLogged) {
        setIsLogged(true);
        setLogId(res.data.log.id);
        setWorkoutPlanId(res.data.log.workoutPlanId);
        setWorkoutPlanName(res.data.log.workoutPlanName);
        
        // Map log entries
        setLogEntries(res.data.log.entries.map((e) => ({
          exerciseId: e.exerciseId,
          exerciseName: e.exerciseName,
          muscleGroup: e.muscleGroup || "",
          actualSets: e.actualSets,
          actualReps: e.actualReps,
          weight: e.weight
        })));
      } else {
        setIsLogged(false);
        setLogId(null);
        if (res.data.template) {
          setWorkoutPlanId(res.data.template.id);
          setWorkoutPlanName(res.data.template.name);
          // Auto-fill log entries from template
          setLogEntries(res.data.template.exercises.map((we) => ({
            exerciseId: we.exerciseId,
            exerciseName: we.exerciseName,
            muscleGroup: we.muscleGroup || "",
            actualSets: "",
            actualReps: "",
            weight: ""
          })));
        } else {
          setWorkoutPlanId(null);
          setWorkoutPlanName("Unplanned");
          setLogEntries([]);
        }
      }
    } catch (err) {
      setError("Failed to fetch today's status");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Load a template manually (e.g. from dropdown if today has no template)
  const handleLoadTemplate = (planId) => {
    if (!planId) return;
    const plan = weeklyPlans.find((p) => p.id === parseInt(planId));
    if (plan) {
      setWorkoutPlanId(plan.id);
      setWorkoutPlanName(plan.name);
      setLogEntries(plan.exercises.map((we) => ({
        exerciseId: we.exerciseId,
        exerciseName: we.exerciseName,
        muscleGroup: we.muscleGroup || "",
        actualSets: "",
        actualReps: "",
        weight: ""
      })));
      setMessage(`Loaded "${plan.name}" plan template!`);
      setTimeout(() => setMessage(""), 3000);
    }
  };

  const handleAddExerciseRow = () => {
    setLogEntries([
      ...logEntries,
      { exerciseId: "", muscleGroup: "", actualSets: "", actualReps: "", weight: "" }
    ]);
  };

  const handleUpdateMuscleGroup = (idx, muscleGroup) => {
    const updated = [...logEntries];
    updated[idx] = { ...updated[idx], muscleGroup, exerciseId: "" };
    setLogEntries(updated);
  };

  const handleUpdateEntryRow = (idx, field, value) => {
    const updated = [...logEntries];
    updated[idx][field] = value;
    setLogEntries(updated);
  };

  const handleRemoveEntryRow = (idx) => {
    setLogEntries(logEntries.filter((_, i) => i !== idx));
  };

  const getFilteredExercises = (muscleGroup) => {
    if (!muscleGroup) return [];
    return availableExercises.filter((ex) => ex.muscleGroup === muscleGroup);
  };

  const handleSubmitWorkout = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    if (logEntries.length === 0) {
      setError("Please add at least one exercise entry to log your workout.");
      return;
    }

    if (logEntries.some((entry) => !entry.exerciseId)) {
      setError("Please select an exercise for all logged rows.");
      return;
    }

    setSaving(true);
    try {
      const payload = {
        id: logId, // Will be null for new logs, and present for edits
        userId: parseInt(userId),
        date: getLocalDateString(),
        workoutPlanId: workoutPlanId,
        entries: logEntries.map((e) => ({
          exerciseId: parseInt(e.exerciseId),
          actualSets: parseInt(e.actualSets),
          actualReps: parseInt(e.actualReps),
          weight: parseFloat(e.weight)
        }))
      };

      await API.post("/log/save", payload);
      setMessage("Workout saved successfully! Keep up the great work! 💪");
      setIsLogged(true);
      fetchTodayStatus();
    } catch (err) {
      setError("Failed to save today's workout log.");
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleEditLoggedWorkout = () => {
    setIsLogged(false);
  };

  if (loading) {
    return <div className="loading">Loading today's routine...</div>;
  }

  return (
    <div className="today-workout-container">
      <h2>🏋️ Today's Workout Logging</h2>
      <p className="subtitle">Record what you actually lift today. Performance is saved separate from weekly templates.</p>

      {message && <div className="success-message">{message}</div>}
      {error && <div className="error-message">{error}</div>}

      {isLogged ? (
        <div className="logged-success-card">
          <div className="success-header">
            <h3>🎉 Workout Completed Today!</h3>
            <span className="log-date">{getLocalDateString()}</span>
          </div>
          <p className="plan-tag">Routine: <strong>{workoutPlanName}</strong></p>

          <div className="logged-entries-table">
            <div className="table-headers">
              <span>Exercise</span>
              <span>Sets</span>
              <span>Reps</span>
              <span>Weight</span>
            </div>
            {logEntries.map((entry, idx) => (
              <div key={idx} className="table-row">
                <span className="exercise-name">{entry.exerciseName || availableExercises.find(e => e.id === entry.exerciseId)?.name}</span>
                <span>{entry.actualSets}</span>
                <span>{entry.actualReps}</span>
                <span className="weight-tag">{entry.weight} kg</span>
              </div>
            ))}
          </div>

          <div className="logged-actions">
            <button onClick={handleEditLoggedWorkout} className="btn-secondary">
              ✏️ Edit Workout Log
            </button>
            <button onClick={() => onNavigate("history")} className="btn-primary">
              📜 View History
            </button>
          </div>
        </div>
      ) : (
        <div className="log-editor-card">
          <div className="log-meta">
            <span>Today is <strong>{dayOfWeek.charAt(0) + dayOfWeek.slice(1).toLowerCase()}</strong></span>
            <span>Target Plan: <strong>{workoutPlanName}</strong></span>
          </div>

          {/* Quick template selector in case no plan is set for today, or they want a different one */}
          <div className="quick-template-selector">
            <label>Load different routine template:</label>
            <select onChange={(e) => handleLoadTemplate(e.target.value)} defaultValue="">
              <option value="">-- Choose Template --</option>
              {weeklyPlans.map((plan) => (
                <option key={plan.id} value={plan.id}>
                  {plan.dayOfWeek.charAt(0) + plan.dayOfWeek.slice(1).toLowerCase()}: {plan.name}
                </option>
              ))}
            </select>
          </div>

          <form onSubmit={handleSubmitWorkout}>
            <div className="log-entries-section">
              {logEntries.length === 0 ? (
                <div className="empty-log-placeholder">
                  <p>No exercises loaded. Select a template above or add custom exercises to get started.</p>
                  <button type="button" onClick={handleAddExerciseRow} className="btn-add-exercise">
                    + Add Custom Exercise
                  </button>
                </div>
              ) : (
                <>
                  <div className="log-table-headers log-table-headers--dual">
                    <span>Muscle Group</span>
                    <span>Exercise</span>
                    <span>Actual Sets</span>
                    <span>Actual Reps</span>
                    <span>Weight (kg)</span>
                    <span>Action</span>
                  </div>

                  {logEntries.map((entry, idx) => (
                    <div key={idx} className="log-entry-row log-entry-row--dual">
                      {/* Dropdown A: Muscle Group */}
                      <select
                        value={entry.muscleGroup || ""}
                        onChange={(e) => handleUpdateMuscleGroup(idx, e.target.value)}
                        disabled={saving}
                        className="select-muscle-group"
                      >
                        <option value="">Select Muscle Group</option>
                        {MUSCLE_GROUPS.map((mg) => (
                          <option key={mg} value={mg}>{mg}</option>
                        ))}
                      </select>

                      {/* Dropdown B: Exercise (dependent on Muscle Group) */}
                      <select
                        value={entry.exerciseId}
                        onChange={(e) =>
                          handleUpdateEntryRow(idx, "exerciseId", e.target.value)
                        }
                        disabled={saving || !entry.muscleGroup}
                        className={!entry.muscleGroup ? "select-disabled" : ""}
                      >
                        <option value="">
                          {entry.muscleGroup ? "Select Exercise" : "— Pick muscle group first —"}
                        </option>
                        {getFilteredExercises(entry.muscleGroup).map((ex) => (
                          <option key={ex.id} value={ex.id}>
                            {ex.name}
                          </option>
                        ))}
                      </select>

                      <input
                        type="number"
                        placeholder="Sets"
                        min="1"
                        value={entry.actualSets}
                        onChange={(e) =>
                          handleUpdateEntryRow(idx, "actualSets", e.target.value)
                        }
                        disabled={saving}
                      />

                      <input
                        type="number"
                        placeholder="Reps"
                        min="1"
                        value={entry.actualReps}
                        onChange={(e) =>
                          handleUpdateEntryRow(idx, "actualReps", e.target.value)
                        }
                        disabled={saving}
                      />

                      <input
                        type="number"
                        placeholder="Weight"
                        min="0"
                        step="0.5"
                        value={entry.weight}
                        onChange={(e) =>
                          handleUpdateEntryRow(idx, "weight", e.target.value)
                        }
                        disabled={saving}
                      />

                      <button
                        type="button"
                        onClick={() => handleRemoveEntryRow(idx)}
                        className="btn-remove-row"
                        disabled={saving}
                      >
                        ✕
                      </button>
                    </div>
                  ))}

                  <button
                    type="button"
                    onClick={handleAddExerciseRow}
                    className="btn-add-row"
                    disabled={saving}
                  >
                    + Add Custom Exercise
                  </button>
                </>
              )}
            </div>

            <div className="log-form-actions">
              <button
                type="submit"
                className="btn-primary btn-save-log"
                disabled={saving}
              >
                {saving ? "Saving Workout Log..." : "Submit / Log Workout"}
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}

export default TodayWorkout;
