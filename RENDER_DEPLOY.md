# Deploy GymTrack on Render

## Why you saw `mvn: command not found`

Render **does not support a native Java/Maven runtime**. Supported native runtimes are Node, Python, Ruby, Go, Rust, and Elixir only.

If you create a Web Service from the **repo root** without setting **Root Directory**, Render finds `gym-frontend/package.json` and uses **Node.js**. Then `mvn clean install` fails because Maven is not installed.

**Fix:** Deploy the backend from `gym-backend/` using **Docker** (Maven runs inside the Docker build).

---

## Repository layout (correct for monorepo)

```
GymTrack/                    ← Git repo root
├── gym-backend/             ← Spring Boot (pom.xml, Dockerfile) — deploy THIS on Render
│   ├── pom.xml
│   ├── Dockerfile
│   ├── mvnw
│   └── src/
├── gym-frontend/            ← React app (CRA) — deploy on Vercel/Netlify/Render Static
│   ├── package.json
│   ├── src/
│   └── public/
└── render.yaml              ← optional Blueprint (backend)
```

Equivalent clean layout (optional rename later):

| Current           | Suggested alias |
|-------------------|-----------------|
| `gym-backend/`    | `backend/`      |
| `gym-frontend/` | `frontend/` |

Renaming is optional; Render only needs **Root Directory = `gym-backend`**.

---

## Part 1: MySQL database (required)

Render does not include MySQL on the free tier. Use one of:

- [PlanetScale](https://planetscale.com) (MySQL-compatible)
- [Railway](https://railway.app) MySQL plugin
- [Aiven](https://aiven.io) free MySQL
- Any hosted MySQL with a public URL

Note your JDBC URL, username, and password.

Example URL format:

```
jdbc:mysql://HOST:3306/gym_db?useSSL=true&requireSSL=true&serverTimezone=UTC
```

---

## Part 2: Deploy backend on Render (Docker)

### Option A — Dashboard (recommended)

1. Push this repo to GitHub/GitLab.
2. Go to [render.com](https://render.com) → **New +** → **Web Service**.
3. Connect the **GymTrack** repository.
4. Configure:

| Setting | Value |
|---------|--------|
| **Name** | `gymtrack-api` (any name) |
| **Region** | Closest to you |
| **Branch** | `main` (or your default) |
| **Root Directory** | `gym-backend` |
| **Runtime** | **Docker** (not Node, not “Java”) |
| **Dockerfile Path** | `Dockerfile` (default; relative to root dir) |
| **Build Command** | *(leave empty — Docker builds the app)* |
| **Start Command** | *(leave empty — Dockerfile `ENTRYPOINT` starts the JAR)* |

5. **Environment variables** (Environment → Add):

| Key | Example |
|-----|---------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://...` |
| `SPRING_DATASOURCE_USERNAME` | your DB user |
| `SPRING_DATASOURCE_PASSWORD` | your DB password |

6. Click **Create Web Service** and wait for the build.
7. Test: `https://YOUR-SERVICE.onrender.com/exercise/all` should return JSON (or `[]`).

### Option B — Blueprint (`render.yaml`)

1. In Render dashboard: **New +** → **Blueprint**.
2. Connect the repo; Render reads root `render.yaml`.
3. Set database env vars when prompted.
4. Deploy.

---

## Part 3: Deploy frontend (separate service)

Do **not** deploy the React app as the same Java service.

### Render Static Site (optional)

| Setting | Value |
|---------|--------|
| **Root Directory** | `gym-frontend` |
| **Build Command** | `npm install && npm run build` |
| **Publish Directory** | `build` |
| **Environment** | `REACT_APP_API_URL=https://YOUR-SERVICE.onrender.com` |

### Vercel / Netlify (also fine)

- Root: `gym-frontend`
- Build: `npm run build`
- Output: `build`
- Env: `REACT_APP_API_URL=https://YOUR-SERVICE.onrender.com`

---

## Local dev after config change

1. Copy `gym-backend/src/main/resources/application-local.properties.example` → `application-local.properties`
2. Fill in your local MySQL password.
3. Run backend:

```bash
cd gym-backend
./mvnw spring-boot:run
```

Spring Boot loads `application-local.properties` automatically when present (add to `.gitignore` — already listed).

To activate local profile explicitly:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Add `spring.config.import=optional:classpath:application-local.properties` if needed — actually Spring Boot auto-loads application-local.properties by default in 2.4+.

---

## Manual build commands (local / CI)

From `gym-backend/`:

```bash
./mvnw clean install -DskipTests
java -jar target/gym-backend.jar
```

On Windows:

```cmd
mvnw.cmd clean install -DskipTests
java -jar target\gym-backend.jar
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `mvn: command not found` | Runtime is Node — set **Root Directory** to `gym-backend` and **Runtime** to **Docker** |
| Build fails: no `pom.xml` | Root Directory is wrong — must be `gym-backend` |
| App crashes on start | Check DB env vars and that MySQL allows remote connections |
| CORS errors from frontend | Backend already allows all origins; set `REACT_APP_API_URL` to the Render backend URL (no trailing slash) |
| 502 / spin down | Free tier sleeps after inactivity; first request may be slow |

---

## Security note

Do not commit database passwords. Use Render environment variables or local `application-local.properties` (gitignored).
