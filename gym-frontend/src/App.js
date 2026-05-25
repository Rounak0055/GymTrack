import { useState, useEffect } from "react";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import WeeklyPlan from "./pages/WeeklyPlan";
import TodayWorkout from "./pages/TodayWorkout";
import History from "./pages/History";
import "./App.css";

function App() {
  const [page, setPage] = useState("login"); // login, register, dashboard, plan, today, history
  const [userId, setUserId] = useState(null);
  const [userName, setUserName] = useState(null);

  // ✅ Check if user is already logged in
  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    const storedUserName = localStorage.getItem("userName");
    if (storedUserId) {
      setUserId(storedUserId);
      setUserName(storedUserName);
      setPage("dashboard");
    }
  }, []);

  const handleLoginSuccess = (id, name) => {
    setUserId(id);
    setUserName(name);
    localStorage.setItem("userId", id);
    localStorage.setItem("userName", name);
    setPage("dashboard");
  };

  const handleLogout = () => {
    setUserId(null);
    setUserName(null);
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    setPage("login");
  };

  return (
    <div className="app-container">
      {page === "login" && (
        <Login
          onLoginSuccess={handleLoginSuccess}
          onSwitchToRegister={() => setPage("register")}
        />
      )}
      {page === "register" && (
        <Register
          onRegisterSuccess={() => setPage("login")}
          onSwitchToLogin={() => setPage("login")}
        />
      )}
      {["dashboard", "plan", "today", "history"].includes(page) && (
        <div className="main-layout">
          {/* ✅ Styled Navigation Bar */}
          <nav className="navbar">
            <div className="nav-logo">💪 GymTrack</div>
            <div className="nav-links">
              <button
                className={`nav-btn ${page === "dashboard" ? "active" : ""}`}
                onClick={() => setPage("dashboard")}
              >
                Dashboard
              </button>
              <button
                className={`nav-btn ${page === "plan" ? "active" : ""}`}
                onClick={() => setPage("plan")}
              >
                Weekly Plan
              </button>
              <button
                className={`nav-btn ${page === "today" ? "active" : ""}`}
                onClick={() => setPage("today")}
              >
                Today's Workout
              </button>
              <button
                className={`nav-btn ${page === "history" ? "active" : ""}`}
                onClick={() => setPage("history")}
              >
                History
              </button>
            </div>
            <div className="nav-user">
              <span className="welcome-text">Hi, {userName}!</span>
              <button onClick={handleLogout} className="btn-logout-nav">
                Logout
              </button>
            </div>
          </nav>

          <div className="content-container">
            {page === "dashboard" && (
              <Dashboard
                userId={userId}
                userName={userName}
                onNavigate={(p) => setPage(p)}
              />
            )}
            {page === "plan" && (
              <WeeklyPlan
                userId={userId}
              />
            )}
            {page === "today" && (
              <TodayWorkout
                userId={userId}
                onNavigate={(p) => setPage(p)}
              />
            )}
            {page === "history" && (
              <History
                userId={userId}
              />
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default App;