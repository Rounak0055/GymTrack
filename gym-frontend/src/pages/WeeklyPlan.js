import { useState, useEffect } from "react";
import API from "../services/api";

const DAYS_OF_WEEK = [
  "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
];

function WeeklyPlan({ userId }) {
  const [selectedDay, setSelectedDay] = useState("MONDAY");
  const [allPlans, setAllPlans] = useState({});
  const [dayPlanName, setDayPlanName] = useState("");
  const [plannedExercises, setPlannedExercises] = useState([]);
  const [availableExercises, setAvailableExercises] = useState([]);
  const [loading, setLoading] = useState(false);
  const [exercisesLoading, setExercisesLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    fetchAvailableExercises();
    fetchWeeklyPlans();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Update fields when selected tab changes
  useEffect(() => {
    const activePlan = allPlans[selectedDay];
    if (activePlan) {
      setDayPlanName(activePlan.name || "");
      setPlannedExercises(activePlan.exercises || []);
    } else {
      setDayPlanName("");
      setPlannedExercises([]);
    }
    setMessage("");
    setError("");
  }, [selectedDay, allPlans]);

  const fetchAvailableExercises = async () => {
    try {
      const res = await API.get("/exercise/all");
      setAvailableExercises(res.data);
      setExercisesLoading(false);
    } catch (err) {
      setError("Failed to load exercises database");
      setExercisesLoading(false);
      console.error(err);
    }
  };

  const fetchWeeklyPlans = async () => {
    try {
      const res = await API.get(`/plan/user/${userId}`);
      const plansMap = {};
      res.data.forEach((plan) => {
        plansMap[plan.dayOfWeek] = plan;
      });
      setAllPlans(plansMap);
    } catch (err) {
      console.error("Failed to load weekly plans", err);
    }
  };

  const handleAddExerciseRow = () => {
    setPlannedExercises([
      ...plannedExercises,
      { exerciseId: "" }
    ]);
  };

  const handleUpdateExerciseRow = (idx, field, value) => {
    const updated = [...plannedExercises];
    updated[idx][field] = value;
    setPlannedExercises(updated);
  };

  const handleRemoveExerciseRow = (idx) => {
    setPlannedExercises(plannedExercises.filter((_, i) => i !== idx));
  };

  const handleSavePlan = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    if (!dayPlanName.trim()) {
      setError("Plan name is required (e.g. Chest Day, Rest Day)");
      return;
    }

    // Check if exercises are completely filled
    if (plannedExercises.some((ex) => !ex.exerciseId)) {
      setError("Please select an exercise for all added rows.");
      return;
    }

    setLoading(true);
    try {
      const payload = {
        userId: parseInt(userId),
        dayOfWeek: selectedDay,
        name: dayPlanName,
        exercises: plannedExercises.map((ex) => ({
          exerciseId: parseInt(ex.exerciseId)
        }))
      };

      const res = await API.post("/plan/save", payload);
      
      // Update local state map
      setAllPlans({
        ...allPlans,
        [selectedDay]: res.data
      });
      setMessage(`Successfully saved plan for ${selectedDay}!`);
    } catch (err) {
      setError("Failed to save plan");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="weekly-plan-container">
      <h2>📅 Weekly Workout Planner</h2>
      <p className="subtitle">Choose which exercises to do each day. Sets, reps, and weight are logged when you work out.</p>

      {/* ✅ Tabs for Days of Week */}
      <div className="day-tabs">
        {DAYS_OF_WEEK.map((day) => (
          <button
            key={day}
            className={`day-tab-btn ${selectedDay === day ? "active" : ""}`}
            onClick={() => setSelectedDay(day)}
          >
            {day.charAt(0) + day.slice(1).toLowerCase()}
          </button>
        ))}
      </div>

      <div className="plan-editor-card">
        <h3>Edit Plan for {selectedDay.charAt(0) + selectedDay.slice(1).toLowerCase()}</h3>
        
        {message && <div className="success-message">{message}</div>}
        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSavePlan}>
          <div className="form-group">
            <label>Plan / Workout Name</label>
            <input
              type="text"
              placeholder="e.g. Push Day, Pull Day, Rest & Recovery"
              value={dayPlanName}
              onChange={(e) => setDayPlanName(e.target.value)}
              disabled={loading}
            />
          </div>

          <div className="planned-exercises-section">
            <h4>Planned Exercises</h4>

            {exercisesLoading ? (
              <p>Loading exercise database...</p>
            ) : plannedExercises.length === 0 ? (
              <div className="empty-plan-placeholder">
                <p>No exercises planned for {selectedDay.charAt(0) + selectedDay.slice(1).toLowerCase()}.</p>
                <button
                  type="button"
                  onClick={handleAddExerciseRow}
                  className="btn-add-exercise"
                  disabled={loading}
                >
                  + Add First Exercise
                </button>
              </div>
            ) : (
              <>
                <div className="exercise-headers">
                  <span>Exercise</span>
                  <span>Action</span>
                </div>

                {plannedExercises.map((entry, idx) => (
                  <div key={idx} className="planned-exercise-row">
                    <select
                      value={entry.exerciseId}
                      onChange={(e) =>
                        handleUpdateExerciseRow(idx, "exerciseId", e.target.value)
                      }
                      disabled={loading}
                    >
                      <option value="">Select Exercise</option>
                      {availableExercises.map((ex) => (
                        <option key={ex.id} value={ex.id}>
                          {ex.name} ({ex.muscleGroup})
                        </option>
                      ))}
                    </select>

                    <button
                      type="button"
                      onClick={() => handleRemoveExerciseRow(idx)}
                      className="btn-remove-row"
                      disabled={loading}
                    >
                      ✕
                    </button>
                  </div>
                ))}

                <button
                  type="button"
                  onClick={handleAddExerciseRow}
                  className="btn-add-row"
                  disabled={loading}
                >
                  + Add Exercise
                </button>
              </>
            )}
          </div>

          <button
            type="submit"
            className="btn-primary btn-save-plan"
            disabled={loading || exercisesLoading}
          >
            {loading ? "Saving Plan..." : `Save ${selectedDay.charAt(0) + selectedDay.slice(1).toLowerCase()} Plan`}
          </button>
        </form>
      </div>
    </div>
  );
}

export default WeeklyPlan;
