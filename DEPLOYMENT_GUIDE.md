# 🚀 Deployment Guide - GymTrack

## Full Deployment Instructions

Complete guide to deploy GymTrack to the cloud.

---

## Prerequisites

- Backend deployed and running
- Frontend built and ready
- Domain names (optional)
- Cloud accounts

---

## Option A: Deploy to Vercel (Recommended for Frontend)

### Step 1: Create Production Build
```bash
cd gym-frontend/gym-frontend
npm run build
```

Creates `build/` folder with optimized files.

### Step 2: Install Vercel CLI
```bash
npm install -g vercel
```

### Step 3: Deploy
```bash
vercel
```

Follow the prompts:
- Link to account
- Select project name
- Choose build settings
- Confirm deployment

### Step 4: Configure Environment
In Vercel dashboard:
1. Go to Project Settings
2. Environment Variables
3. Add:
   ```
   REACT_APP_API_URL=https://your-backend-url
   ```

### Step 5: Deploy Production
```bash
vercel --prod
```

**Result**: Your frontend will have public URL like:
```
https://gymtrack.vercel.app
```

---

## Option B: Deploy to Netlify (Alternative)

### Step 1: Build Frontend
```bash
cd gym-frontend/gym-frontend
npm run build
```

### Step 2: Create Netlify Account
- Go to https://netlify.com
- Sign up with GitHub/email
- Connect repository or drag-and-drop build folder

### Step 3: Deploy
```bash
npm install -g netlify-cli
netlify deploy --prod --dir=build
```

### Step 4: Configure
In Netlify dashboard:
1. Site settings
2. Build & deploy
3. Environment variables
4. Add: `REACT_APP_API_URL=https://your-backend-url`

---

## Backend Deployment Options

### Option A: Deploy to Railway

1. Go to https://railway.app
2. Sign up with GitHub
3. Create new project
4. Connect GitHub repository
5. Configure:
   - MySQL database
   - Port: 8080
   - Environment variables: database URL, username, password

### Option B: Deploy to Render

**Important:** Render has no native Java runtime. Use **Docker** and **Root Directory `gym-backend`**.

See the full guide: **[RENDER_DEPLOY.md](./RENDER_DEPLOY.md)**

Quick settings:

| Setting | Value |
|---------|--------|
| Root Directory | `gym-backend` |
| Runtime | **Docker** |
| Build / Start commands | *(empty — uses `gym-backend/Dockerfile`)* |
| Env vars | `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `SPRING_PROFILES_ACTIVE=prod` |

### Option C: Deploy to Heroku (Requires Card)

1. Go to https://heroku.com
2. Create new app
3. Connect GitHub
4. Configure environment variables
5. Deploy

---

## Database Deployment

### Use Cloud MySQL Services:

#### Option 1: AWS RDS MySQL
- Go to AWS RDS
- Create MySQL instance
- Get connection string
- Update application.properties

#### Option 2: ClearDB (Simple)
- Free tier available
- Easy setup
- Use provided connection string

#### Option 3: PlanetScale (MySQL Compatible)
- Modern MySQL serverless
- Free tier available
- Connection string provided

---

## Complete Flow (End-to-End)

### 1. Deploy Backend First
```bash
# Option A: Railway
# Option B: Render
# Option C: Heroku
```
Get backend URL: `https://your-backend.onrender.com`

### 2. Deploy MySQL
- Use AWS RDS, ClearDB, or PlanetScale
- Get connection string

### 3. Update Backend Config
Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://your-db-host/gym_db
spring.datasource.username=your-user
spring.datasource.password=your-password
```

### 4. Deploy Frontend
```bash
cd gym-frontend/gym-frontend
npm run build
vercel --prod
```

### 5. Update Frontend API URL
In Vercel environment variables:
```
REACT_APP_API_URL=https://your-backend.onrender.com
```

### 6. Test
Open frontend URL and test:
- Register
- Login
- Create workout
- Delete workout

---

## Verification Checklist

### Backend:
- [ ] Backend is running
- [ ] Database is connected
- [ ] APIs responding
- [ ] CORS enabled
- [ ] Check: `https://your-backend/exercise/all`

### Frontend:
- [ ] Frontend is deployed
- [ ] Can load at URL
- [ ] Can register
- [ ] Can login
- [ ] Can create workout
- [ ] Can see workouts

### Integration:
- [ ] Frontend API calls working
- [ ] No CORS errors
- [ ] Login/logout working
- [ ] Workouts persisting

---

## Production URLs Template

Once deployed, you'll have:

```
Frontend: https://gymtrack.vercel.app
Backend:  https://gym-backend.onrender.com
Database: mysql://user:pass@host:3306/gym_db
```

Example production flow:
```
User visits: https://gymtrack.vercel.app
↓
Clicks Register
↓
Creates account (calls Backend API)
↓
Logs in
↓
Creates workout
↓
Workout stored in Cloud MySQL
↓
Dashboard shows workout
```

---

## Monitoring

### Monitor Backend:
- Render/Railway dashboards
- Check logs for errors
- Monitor database performance

### Monitor Frontend:
- Vercel analytics
- Error tracking
- Performance metrics

### Monitor Database:
- Connection pooling
- Query performance
- Storage usage

---

## Common Issues

### CORS Error:
- Ensure backend has: `@CrossOrigin(origins = "*")`
- Or specify frontend URL: `@CrossOrigin(origins = "https://your-frontend.com")`

### Database Connection Error:
- Check credentials in application.properties
- Verify database is accessible from cloud
- Check firewall rules
- Allow all IPs: 0.0.0.0/0

### Frontend Can't Find Backend:
- Verify API URL is correct in env variables
- Check backend is running
- Test API directly in browser

### MySQL Too Many Connections:
- Increase max connections
- Use connection pooling
- Restart database service

---

## Cost Estimate (Monthly)

| Service | Free Tier | Paid |
|---------|-----------|------|
| Vercel (Frontend) | ✅ $0 | $20+/mo |
| Render (Backend) | ~$5/mo | $7+/mo |
| Railway (Backend) | ✅ $5/mo | Pay per use |
| ClearDB (MySQL) | ✅ Free | $9.99+/mo |
| PlanetScale (MySQL) | ✅ Free | $29/mo |
| AWS RDS | Limited | $15+/mo |

**Estimated Free/Cheap Tier Cost: $5-10/month**

---

## Scaling (Future)

If you need to scale:
1. Add CDN (CloudFlare)
2. Database optimization
3. Backend load balancing
4. Caching (Redis)
5. Database read replicas

---

## Useful Commands

### Build Frontend:
```bash
npm run build
```

### Build Backend:
```bash
mvn clean package
```

### Test Production Build Locally:
```bash
npm run build
npx serve -s build
```

### View Production Build Size:
```bash
npm run build
npm install -g source-map-explorer
source-map-explorer 'build/static/js/*.js'
```

---

## Support & Monitoring

### Recommended Tools:
- Sentry (error tracking)
- DataDog (monitoring)
- New Relic (performance)
- Loggly (log management)

All free tiers available!

---

## Final Checklist Before Launch

- [ ] Backend deployed and running
- [ ] Database connected
- [ ] Frontend deployed
- [ ] API URLs configured
- [ ] All features tested
- [ ] Error handling working
- [ ] Database backups setup
- [ ] Monitoring enabled
- [ ] Documentation updated
- [ ] Users can register/login

---

## After Launch

1. **Monitor**: Check logs daily
2. **Backup**: Set up database backups
3. **Update**: Apply security patches
4. **Optimize**: Monitor performance
5. **Scale**: Add resources if needed

---

## Example: Deployed URLs

Once you've completed deployment, you'll have something like:

```
🌐 Frontend:  https://gymtrack.vercel.app
🔧 Backend:   https://gym-backend.onrender.com
📊 Database:  mysql://user@db-host/gym_db
```

Share these URLs with your users!

---

## Support Resources

- Vercel Docs: https://vercel.com/docs
- Render Docs: https://render.com/docs
- Railway Docs: https://docs.railway.app
- MySQL Docs: https://dev.mysql.com/doc

---

## Congratulations! 🎉

Your GymTrack is now live on the internet!

**Working Application:** ✅
**Database Online:** ✅
**Frontend Accessible:** ✅
**Ready for Users:** ✅

Share your public URL and let people test it! 🚀
