# GymTrack

Full-stack workout tracker: Spring Boot API + React frontend.

## Project structure

```
gym-backend/              Spring Boot API (deploy to Render with Docker)
gym-frontend/              React app (deploy to Vercel, Netlify, or Render Static)
```

## Quick start (local)

**Backend**

```bash
cd gym-backend
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
# Edit application-local.properties with your MySQL credentials
./mvnw spring-boot:run
```

**Frontend**

```bash
cd gym-frontend
npm install
npm start
```

API: http://localhost:8080 — UI: http://localhost:3000

## Deploy

- **Backend on Render:** [RENDER_DEPLOY.md](./RENDER_DEPLOY.md)
- **General deployment:** [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)
