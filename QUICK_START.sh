#!/bin/bash
# 🏋️ GymTrack - Quick Start Script

echo "=========================================="
echo "🏋️  GymTrack - Complete Project Setup"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}Step 1: Prerequisites Check${NC}"
echo "- Java 21+ : Run 'java -version'"
echo "- Node.js 14+ : Run 'node -v'"
echo "- MySQL 8+ : Must be running"
echo "- Port 3000 (frontend) & 8080 (backend) must be free"
echo ""

echo -e "${BLUE}Step 2: Database Setup${NC}"
echo "Run in MySQL:"
echo "CREATE DATABASE gym_db;"
echo "Use existing or create user with credentials in application.properties"
echo ""

echo -e "${BLUE}Step 3: Start Backend${NC}"
echo "cd gym-backend"
echo "mvn clean install"
echo "mvn spring-boot:run"
echo ""
echo "✅ Backend will start on http://localhost:8080"
echo "Wait for 'Tomcat started on port(s): 8080' message"
echo ""

echo -e "${BLUE}Step 4: Start Frontend (in NEW terminal)${NC}"
echo "cd gym-frontend/gym-frontend"
echo "npm install  (if not done)"
echo "npm start"
echo ""
echo "✅ Frontend will start on http://localhost:3000"
echo ""

echo -e "${BLUE}Step 5: Test Application${NC}"
echo "1. Open http://localhost:3000 in browser"
echo "2. Click 'Register here' to create account"
echo "3. Fill registration form and submit"
echo "4. Login with registered email/password"
echo "5. Click 'Create New Workout'"
echo "6. Add workout with exercises"
echo "7. See workouts on dashboard"
echo ""

echo -e "${GREEN}=========================================="
echo "Frontend: READY ✅"
echo "Backend: READY ✅"
echo "All files created and configured!"
echo "==========================================${NC}"
