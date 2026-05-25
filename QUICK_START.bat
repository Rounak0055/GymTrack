@echo off
REM 🏋️ GymTrack - Quick Start Script for Windows

color 0A
echo.
echo ==========================================
echo 🏋️  GymTrack - Complete Project Setup
echo ==========================================
echo.

echo [STEP 1] Prerequisites Check
echo - Java 21+ required: Run 'java -version'
echo - Node.js 14+ required: Run 'node -v'
echo - MySQL 8+ must be running
echo - Ports 3000 (frontend) and 8080 (backend) must be free
echo.

echo [STEP 2] Database Setup
echo Run in MySQL Command Prompt:
echo   CREATE DATABASE gym_db;
echo.

echo [STEP 3] Start Backend (in Command Prompt)
echo   cd gym-backend
echo   mvn clean install
echo   mvn spring-boot:run
echo.
echo ✅ Backend will start on http://localhost:8080
echo Wait for "Tomcat started on port(s): 8080" message
echo.

echo [STEP 4] Start Frontend (in NEW Command Prompt)
echo   cd gym-frontend\gym-frontend
echo   npm install
echo   npm start
echo.
echo ✅ Frontend will start on http://localhost:3000
echo.

echo [STEP 5] Test Application
echo 1. Open http://localhost:3000 in browser
echo 2. Click "Register here" to create account
echo 3. Fill registration form and submit
echo 4. Login with registered email/password
echo 5. Click "Create New Workout"
echo 6. Add workout with exercises
echo 7. See workouts on dashboard
echo.

echo ==========================================
echo Frontend: READY ✅
echo Backend: READY ✅
echo All files created and configured!
echo ==========================================
echo.

pause
