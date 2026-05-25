import { useState, useEffect } from "react";
import API from "../services/api";

const DAYS_OF_WEEK = [
  "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
];

function Dashboard({ userId, userName, onNavigate }) {
  const [weeklyPlans, setWeeklyPlans] = useState({});
  const [todayLog, setTodayLog] = useState(null);
  const [todayPlan, setTodayPlan] = useState(null);
  const [dayOfWeek, setDayOfWeek] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchDashboardData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      
      // 1. Fetch weekly plans
      const plansRes = await API.get(`/plan/user/${userId}`);
      const plansMap = {};
      plansRes.data.forEach((plan) => {
        plansMap[plan.dayOfWeek] = plan;
      });
      setWeeklyPlans(plansMap);

      // 2. Fetch today status
      const todayRes = await API.get(`/log/user/${userId}/today`);
      setDayOfWeek(todayRes.data.dayOfWeek || "");

      if (todayRes.data.isLogged) {
        setTodayLog(todayRes.data.log);
      } else {
        setTodayLog(null);
        setTodayPlan(todayRes.data.template);
      }
    } catch (err) {
      setError("Failed to load dashboard statistics");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const getDayLabel = (day) => {
    return day.charAt(0) + day.slice(1).toLowerCase();
  };

  if (loading) {
    return <div className="loading">Initializing dashboard...</div>;
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header-simple">
        <h2>⚡ Welcome back, {userName}!</h2>
        <p>Keep track of your consistency and reach your goals.</p>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="dashboard-grid">
        
        {/* ✅ Today's Status Card */}
        <div className="dashboard-card today-status-card">
          <h3>🏋️ Today's Focus</h3>
          <p className="day-text">Today is <strong>{getDayLabel(dayOfWeek)}</strong></p>

          {todayLog ? (
            <div className="status-complete">
              <span className="success-badge">Completed ✓</span>
              <p>You logged a workout for today. Awesome consistency!</p>
              <button onClick={() => onNavigate("history")} className="btn-card-action">
                View Today's Log
              </button>
            </div>
          ) : todayPlan ? (
            <div className="status-pending">
              <span className="pending-badge">Planned</span>
              <p>Today's planned routine is: <strong>{todayPlan.name}</strong></p>
              <p className="mini-details">{todayPlan.exercises.length} exercises scheduled.</p>
              <button onClick={() => onNavigate("today")} className="btn-card-action btn-primary-action">
                Start Log Session
              </button>
            </div>
          ) : (
            <div className="status-rest">
              <span className="rest-badge">Rest / Custom</span>
              <p>No repeating plan scheduled for today.</p>
              <button onClick={() => onNavigate("today")} className="btn-card-action">
                Log custom workout
              </button>
            </div>
          )}
        </div>

        {/* ✅ Quick Stats/Actions Card */}
        <div className="dashboard-card quick-actions-card">
          <h3>🚀 Quick Actions</h3>
          <div className="actions-vertical">
            <button onClick={() => onNavigate("today")} className="btn-action-row">
              💪 Log Workout Session
            </button>
            <button onClick={() => onNavigate("plan")} className="btn-action-row">
              📅 Modify Weekly Plan
            </button>
            <button onClick={() => onNavigate("history")} className="btn-action-row">
              📜 Check Exercise Logs
            </button>
          </div>
        </div>

      </div>

      {/* ✅ Weekly Schedule Summary */}
      <div className="dashboard-section weekly-routine-section">
        <h3>📅 Your Weekly Schedule</h3>
        <p className="section-desc">Here is your repeating weekly schedule template:</p>
        
        <div className="weekly-schedule-grid">
          {DAYS_OF_WEEK.map((day) => {
            const plan = weeklyPlans[day];
            return (
              <div key={day} className={`schedule-day-card ${dayOfWeek === day ? "current-day-highlight" : ""}`}>
                <div className="schedule-day-name">
                  {getDayLabel(day)}
                  {dayOfWeek === day && <span className="today-label">TODAY</span>}
                </div>
                {plan ? (
                  <div className="schedule-day-plan">
                    <div className="plan-name">{plan.name}</div>
                    <div className="plan-exercises-count">{plan.exercises.length} exercises</div>
                  </div>
                ) : (
                  <div className="schedule-day-plan rest-plan">
                    <div className="plan-name">Rest Day</div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;