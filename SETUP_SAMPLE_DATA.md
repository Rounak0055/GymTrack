# 🏋️ Setup Sample Data - Backend

To test the frontend, you need some exercises in the database.

---

## Method 1: API Calls (Easiest)

Once backend is running on `http://localhost:8080`:

### Add Exercises via API

Use Postman or curl to add exercises:

#### Using Postman:
1. Open Postman
2. New Request
3. Method: POST
4. URL: `http://localhost:8080/exercise/add`
5. Body (JSON):
```json
{
  "name": "Bench Press",
  "muscleGroup": "Chest"
}
```
6. Click Send

### Sample Exercises to Add:

```
Chest:
- Bench Press
- Incline Press
- Dumbbell Fly

Back:
- Barbell Row
- Lat Pulldown
- Deadlift

Legs:
- Barbell Squat
- Leg Press
- Leg Curl

Shoulders:
- Military Press
- Lateral Raise
- Face Pull

Arms:
- Barbell Curl
- Tricep Dips
- Hammer Curl
```

#### Using cURL (Windows):
```cmd
curl -X POST http://localhost:8080/exercise/add -H "Content-Type: application/json" -d "{\"name\":\"Bench Press\",\"muscleGroup\":\"Chest\"}"
```

#### Using cURL (Mac/Linux):
```bash
curl -X POST http://localhost:8080/exercise/add \
  -H "Content-Type: application/json" \
  -d '{"name":"Bench Press","muscleGroup":"Chest"}'
```

---

## Method 2: SQL Directly

Connect to MySQL and run:

```sql
USE gym_db;

INSERT INTO exercises (name, muscle_group) VALUES
('Bench Press', 'Chest'),
('Incline Press', 'Chest'),
('Dumbbell Fly', 'Chest'),
('Barbell Row', 'Back'),
('Lat Pulldown', 'Back'),
('Deadlift', 'Back'),
('Barbell Squat', 'Legs'),
('Leg Press', 'Legs'),
('Leg Curl', 'Legs'),
('Military Press', 'Shoulders'),
('Lateral Raise', 'Shoulders'),
('Face Pull', 'Shoulders'),
('Barbell Curl', 'Arms'),
('Tricep Dips', 'Arms'),
('Hammer Curl', 'Arms');
```

---

## Method 3: Create User Account

### Step 1: Register User
1. Open frontend http://localhost:3000
2. Click "Register here"
3. Fill form:
   - Name: John Doe
   - Email: john@example.com
   - Password: password123
4. Click "Register"
5. Should redirect to login

### Step 2: Login
1. Enter email: john@example.com
2. Enter password: password123
3. Click "Login"
4. Should see dashboard

### Step 3: Create Workout
1. Click "+ Create New Workout"
2. Enter: "Chest Day"
3. Click "+ Add Exercise"
4. Select exercise (if none available, add via API first)
5. Enter: Sets: 3, Reps: 10, Weight: 50
6. Click "Create Workout"
7. Should see workout on dashboard

---

## Verify Setup

### Check Exercises Exist:
```
GET http://localhost:8080/exercise/all
```
Should return JSON array of exercises

### Check User Created:
```sql
SELECT * FROM users;
```
Should show registered user

### Check Workout Created:
```sql
SELECT * FROM workouts;
SELECT * FROM workout_entries;
```
Should show workout and entries

---

## Troubleshooting

### No exercises showing in dropdown?
- Add exercises via API before creating workout
- Check database has exercises: `SELECT * FROM exercises;`

### Backend returning 500 error?
- Check MySQL is running
- Check application.properties has correct DB credentials
- Check Tomcat is started

### Can't login?
- Register first
- Check email/password match exactly
- Check database has user record

---

## Sample Data SQL Script

Create file `sample-data.sql`:

```sql
-- Sample Exercises
INSERT INTO exercises (name, muscle_group) VALUES
('Bench Press', 'Chest'),
('Incline Press', 'Chest'),
('Barbell Row', 'Back'),
('Lat Pulldown', 'Back'),
('Barbell Squat', 'Legs'),
('Leg Press', 'Legs'),
('Military Press', 'Shoulders'),
('Lateral Raise', 'Shoulders'),
('Barbell Curl', 'Arms'),
('Tricep Dips', 'Arms');

-- Sample User
INSERT INTO users (name, email, password) VALUES
('Test User', 'test@example.com', 'password123');

-- Get IDs for next step
-- SELECT id FROM users WHERE email = 'test@example.com';
-- SELECT id FROM exercises;
```

Run with:
```bash
mysql -u root -p gym_db < sample-data.sql
```

Enter password: `rounak@2327`

---

## Frontend Testing Workflow

1. **Setup Data**
   - Add exercises via API or SQL
   - Register user via frontend

2. **Test Login**
   - Login with credentials
   - Verify dashboard loads

3. **Test Workout Creation**
   - Click "Create New Workout"
   - Add exercises
   - Verify workout appears

4. **Test Workout Display**
   - See workout card
   - See exercise details
   - See sets/reps/weight

5. **Test Delete**
   - Click delete button
   - Confirm dialog
   - Verify workout removed

6. **Test Logout**
   - Click logout
   - Verify redirected to login
   - Verify session cleared

---

## API Testing Commands

### Get All Exercises:
```bash
curl http://localhost:8080/exercise/all
```

### Add Exercise:
```bash
curl -X POST http://localhost:8080/exercise/add \
  -H "Content-Type: application/json" \
  -d '{"name":"Squat","muscleGroup":"Legs"}'
```

### Get User Workouts:
```bash
curl http://localhost:8080/workout/user/1
```

### Create Workout:
```bash
curl -X POST http://localhost:8080/workout/create \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "day": "Chest Day",
    "entries": [
      {
        "exerciseId": 1,
        "sets": 3,
        "reps": 10,
        "weight": 50
      }
    ]
  }'
```

---

## Quick Start Data

Fastest way to get data:

### Terminal 1 (with backend running):
```bash
# Add 5 essential exercises
curl -X POST http://localhost:8080/exercise/add -H "Content-Type: application/json" -d '{"name":"Bench Press","muscleGroup":"Chest"}'
curl -X POST http://localhost:8080/exercise/add -H "Content-Type: application/json" -d '{"name":"Barbell Row","muscleGroup":"Back"}'
curl -X POST http://localhost:8080/exercise/add -H "Content-Type: application/json" -d '{"name":"Barbell Squat","muscleGroup":"Legs"}'
curl -X POST http://localhost:8080/exercise/add -H "Content-Type: application/json" -d '{"name":"Military Press","muscleGroup":"Shoulders"}'
curl -X POST http://localhost:8080/exercise/add -H "Content-Type: application/json" -d '{"name":"Barbell Curl","muscleGroup":"Arms"}'
```

### Terminal 2:
```bash
cd gym-frontend/gym-frontend
npm start
```

### Browser:
1. Register account
2. Login
3. Create workout
4. Add exercises
5. Test delete

**Done! Ready for testing!** ✅
