import { useState, useEffect } from "react";
import API from "../services/api";

function History({ userId }) {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [selectedMuscleFilter, setSelectedMuscleFilter] = useState("ALL");
  const [deletingId, setDeletingId] = useState(null);

  useEffect(() => {
    fetchHistory();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchHistory = async () => {
    try {
      setLoading(true);
      const res = await API.get(`/log/user/${userId}/history`);
      setHistory(res.data);
    } catch (err) {
      setError("Failed to load workout history");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const getUniqueMuscleGroups = () => {
    const muscles = new Set();
    history.forEach((log) => {
      log.entries.forEach((entry) => {
        if (entry.muscleGroup) {
          muscles.add(entry.muscleGroup.toUpperCase());
        }
      });
    });
    return ["ALL", ...Array.from(muscles)];
  };

  // Filter logs where at least one entry matches the selected muscle group filter
  const filteredHistory = history.filter((log) => {
    if (selectedMuscleFilter === "ALL") return true;
    return log.entries.some(
      (entry) => entry.muscleGroup && entry.muscleGroup.toUpperCase() === selectedMuscleFilter
    );
  });

  const formatDate = (dateStr) => {
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const date = new Date(dateStr);
    return date.toLocaleDateString(undefined, options);
  };

  const handleDeleteLog = async (log) => {
    const confirmed = window.confirm(
      `Delete the workout log for ${formatDate(log.date)}? This cannot be undone.`
    );
    if (!confirmed) return;

    setError("");
    setMessage("");
    setDeletingId(log.id);
    try {
      await API.delete(`/log/${log.id}`);
      setMessage("Workout log deleted successfully.");
      await fetchHistory();
    } catch (err) {
      setError(err.response?.data?.error || "Failed to delete workout log");
      console.error(err);
    } finally {
      setDeletingId(null);
    }
  };

  if (loading) {
    return <div className="loading">Retrieving your workout history...</div>;
  }

  return (
    <div className="history-container">
      <h2>📜 Workout Log History</h2>
      <p className="subtitle">Track your past workout performances and weights lifted over time.</p>

      {message && <div className="success-message">{message}</div>}
      {error && <div className="error-message">{error}</div>}

      {history.length === 0 ? (
        <div className="empty-history-placeholder">
          <p>No workout history found. Start tracking by logging today's workout!</p>
        </div>
      ) : (
        <>
          {/* ✅ Filter Controls */}
          <div className="filter-controls">
            <label>Filter by Muscle Group:</label>
            <div className="muscle-filter-buttons">
              {getUniqueMuscleGroups().map((muscle) => (
                <button
                  key={muscle}
                  className={`filter-btn ${selectedMuscleFilter === muscle ? "active" : ""}`}
                  onClick={() => setSelectedMuscleFilter(muscle)}
                >
                  {muscle.charAt(0) + muscle.slice(1).toLowerCase()}
                </button>
              ))}
            </div>
          </div>

          {/* ✅ History List */}
          {filteredHistory.length === 0 ? (
            <p className="no-filter-results">No workouts matching the selected muscle filter.</p>
          ) : (
            <div className="history-timeline">
              {filteredHistory.map((log) => (
                <div key={log.id} className="history-card">
                  <div className="history-card-header">
                    <div className="date-info">
                      <span className="calendar-icon">📅</span>
                      <h3>{formatDate(log.date)}</h3>
                    </div>
                    <div className="history-card-actions">
                      <span className="history-plan-badge">{log.workoutPlanName}</span>
                      <button
                        type="button"
                        className="btn-delete-log"
                        onClick={() => handleDeleteLog(log)}
                        disabled={deletingId === log.id}
                      >
                        {deletingId === log.id ? "Deleting..." : "Delete"}
                      </button>
                    </div>
                  </div>

                  <div className="history-entries-list">
                    {log.entries.map((entry) => (
                      <div
                        key={entry.id}
                        className={`history-entry-item ${
                          selectedMuscleFilter !== "ALL" &&
                          entry.muscleGroup &&
                          entry.muscleGroup.toUpperCase() === selectedMuscleFilter
                            ? "highlighted-entry"
                            : ""
                        }`}
                      >
                        <div className="entry-left">
                          <span className="entry-name">{entry.exerciseName}</span>
                          <span className="entry-muscle-tag">{entry.muscleGroup}</span>
                        </div>
                        <div className="entry-right">
                          <span className="entry-stats">
                            <strong>{entry.actualSets}</strong> sets × <strong>{entry.actualReps}</strong> reps
                          </span>
                          <span className="entry-weight">
                            at <strong>{entry.weight}</strong> kg
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default History;
