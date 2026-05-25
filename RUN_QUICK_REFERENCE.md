# 🚀 Run GymTrack - Quick Commands

## Windows Users

### Terminal 1: Start Backend
```batch
cd gym-backend
mvn spring-boot:run
```
Wait for: `Tomcat started on port(s): 8080`

### Terminal 2: Start Frontend
```batch
cd gym-frontend\gym-frontend
npm start
```
Opens: http://localhost:3000

---

## Mac/Linux Users

### Terminal 1: Start Backend
```bash
cd gym-backend
mvn spring-boot:run
```
Wait for: `Tomcat started on port(s): 8080`

### Terminal 2: Start Frontend
```bash
cd gym-frontend/gym-frontend
npm start
```
Opens: http://localhost:3000

---

## First Time Setup

### Install Backend Dependencies
```bash
cd gym-backend
mvn clean install
```

### Install Frontend Dependencies
```bash
cd gym-frontend/gym-frontend
npm install
```

---

## Default Credentials

**Backend Database:**
- Database: gym_db
- User: root
- Password: rounak@2327
- Host: localhost:3306

(Update in `gym-backend/src/main/resources/application.properties` if different)

---

## Test Account

1. Open http://localhost:3000
2. Register: Create new account
3. Login: Use registered email/password
4. Dashboard: Create workout and add exercises

---

## Verify Running

### Backend Check
- Open: http://localhost:8080/exercise/all
- Should see JSON array of exercises

### Frontend Check
- Open: http://localhost:3000
- Should see login page

---

## Common Commands

| Action | Command |
|--------|---------|
| Build Frontend | `npm run build` |
| Test Frontend | `npm test` |
| Clean Backend | `mvn clean` |
| Package Backend | `mvn package` |
| Kill Port 3000 | `lsof -ti:3000 \| xargs kill -9` (Mac/Linux) |
| Kill Port 8080 | `lsof -ti:8080 \| xargs kill -9` (Mac/Linux) |

---

## Troubleshooting

**Port already in use?**
- Windows: `netstat -ano | findstr :3000`
- Mac/Linux: `lsof -i :3000`

**Database connection error?**
- Check MySQL is running
- Check credentials in application.properties
- Create database: `CREATE DATABASE gym_db;`

**Module not found?**
- `npm install` in gym-frontend/gym-frontend
- `mvn clean install` in gym-backend

**Dependencies need update?**
- Frontend: `npm update`
- Backend: `mvn dependency:resolve`

---

## Next Steps

1. ✅ Run both servers
2. ✅ Register account
3. ✅ Create workout
4. ✅ Test all features
5. ✅ Deploy to cloud (Vercel + Railway)

---

## Quick Links

- Frontend Code: `gym-frontend/gym-frontend/src/`
- Backend Code: `gym-backend/src/main/`
- Database: MySQL on localhost:3306
- Frontend URL: http://localhost:3000
- Backend URL: http://localhost:8080

---

**Everything is ready! Just run and test! 🎉**
