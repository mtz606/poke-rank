# GitHub Pages Deployment Guide

## Quick Start

### 1. Set GitHub Secret

1. Go to your GitHub repository
2. **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `VITE_API_BASE_URL`
5. Value: `http://your-ec2-ip:8080/api` (replace with your actual EC2 URL)
6. Click **Add secret**

### 2. Enable GitHub Pages

1. Go to repository **Settings** → **Pages**
2. **Source**: Deploy from a branch
3. **Branch**: `gh-pages` (will be created automatically)
4. **Folder**: `/ (root)`
5. Click **Save**

### 3. Update EC2 Backend CORS

On your EC2 instance, update CORS to allow your GitHub Pages domain:

```bash
# Edit .env file
nano ~/.env

# Add your GitHub Pages URL (replace YOUR_USERNAME)
CORS_ALLOWED_ORIGINS="https://YOUR_USERNAME.github.io,http://localhost:5173"

# Restart backend
sudo systemctl restart pokemon-backend
```

### 4. Deploy

**Option A: Automatic (Recommended)**
```bash
# Just push to main branch
git push origin main
# GitHub Actions will automatically build and deploy
```

**Option B: Manual**
```bash
cd pokemon-rankings-frontend

# Build with EC2 URL
./build-for-github-pages.sh http://your-ec2-ip:8080/api

# Deploy
npm run deploy
```

## Your GitHub Pages URL

Your app will be available at:
- `https://YOUR_USERNAME.github.io/poke-rank/`

## Troubleshooting

### CORS Errors
- Verify EC2 backend CORS includes `https://YOUR_USERNAME.github.io`
- Check backend is running: `curl http://your-ec2-ip:8080/api/auth/test`

### Build Fails
- Check GitHub secret `VITE_API_BASE_URL` is set correctly
- Verify EC2 URL is accessible from internet

### API Calls Fail
- Check EC2 Security Group allows port 8080
- Verify backend is running on EC2
- Check browser console for specific errors
