# GitHub Pages + EC2 Backend Setup

## Architecture

```
┌─────────────────────┐
│   GitHub Pages      │
│   (Frontend)        │──┼──► EC2 Instance (Backend)
│   https://...       │     └──► AWS DynamoDB
└─────────────────────┘
```

## Step 1: Configure EC2 Backend CORS

Your EC2 backend needs to allow requests from your GitHub Pages domain.

### Get Your GitHub Pages URL

Your GitHub Pages URL will be:
- `https://YOUR_USERNAME.github.io/poke-rank/`
- Or custom domain if configured

### Update Backend CORS on EC2

On your EC2 instance, update the `.env` file or environment variables:

```bash
# Add your GitHub Pages URL to CORS
CORS_ALLOWED_ORIGINS="https://YOUR_USERNAME.github.io,http://localhost:5173,http://localhost:3000"
```

Or in `application.properties`:
```properties
spring.web.cors.allowed-origins=https://YOUR_USERNAME.github.io,http://localhost:5173,http://localhost:3000
```

**Important**: Restart your backend service after updating CORS.

## Step 2: Set GitHub Secret for EC2 URL

1. Go to your GitHub repository
2. Settings → Secrets and variables → Actions
3. Click "New repository secret"
4. Name: `VITE_API_BASE_URL`
5. Value: `http://your-ec2-ip:8080/api` (or `https://your-ec2-domain.com/api`)

## Step 3: Enable GitHub Pages

1. Go to repository Settings → Pages
2. Source: Deploy from a branch
3. Branch: `gh-pages` (created automatically by workflow)
4. Folder: `/ (root)`
5. Click Save

## Step 4: Build and Deploy

### Option A: Automatic (GitHub Actions)

The workflow will automatically deploy when you push to `main`:

```bash
# Just push your changes
git add .
git commit -m "Update frontend"
git push origin main
```

The GitHub Action will:
1. Build the frontend with EC2 URL
2. Deploy to GitHub Pages

### Option B: Manual Deployment

```bash
cd pokemon-rankings-frontend

# Set EC2 backend URL
export VITE_API_BASE_URL=http://your-ec2-ip:8080/api

# Build
npm run build

# Deploy
npm run deploy
```

## Step 5: Verify Deployment

1. Visit your GitHub Pages URL: `https://YOUR_USERNAME.github.io/poke-rank/`
2. Open browser DevTools → Network tab
3. Try logging in or making an API call
4. Verify requests go to your EC2 backend

## Troubleshooting

### CORS Errors

**Symptom**: Browser console shows CORS errors

**Solution**: 
1. Verify EC2 backend CORS includes your GitHub Pages URL
2. Check backend logs: `sudo journalctl -u pokemon-backend -f`
3. Ensure backend is running: `curl http://your-ec2-ip:8080/api/auth/test`

### API Calls Fail

**Symptom**: Frontend can't connect to backend

**Check**:
1. EC2 Security Group allows port 8080 from internet
2. Backend is running on EC2
3. EC2 URL is correct in GitHub secret
4. Check browser console for exact error

### Build Fails

**Symptom**: GitHub Action fails

**Check**:
1. GitHub secret `VITE_API_BASE_URL` is set correctly
2. Node.js version matches (should be 20)
3. Check GitHub Actions logs for specific errors

## Environment Variables

### For Local Development

Create `pokemon-rankings-frontend/.env.local`:
```bash
VITE_API_BASE_URL=http://localhost:8080/api
```

### For Production (GitHub Pages)

Set GitHub Secret:
- Name: `VITE_API_BASE_URL`
- Value: `http://your-ec2-ip:8080/api`

Or update `.env.production` file (but GitHub Secret is preferred).

## Quick Reference

```bash
# 1. Update EC2 CORS
# On EC2, edit .env:
CORS_ALLOWED_ORIGINS="https://YOUR_USERNAME.github.io,http://localhost:5173"

# 2. Set GitHub Secret
# Repository → Settings → Secrets → Actions
# VITE_API_BASE_URL = http://your-ec2-ip:8080/api

# 3. Enable GitHub Pages
# Settings → Pages → Deploy from gh-pages branch

# 4. Deploy
git push origin main  # Auto-deploys via GitHub Actions
```

## Security Notes

1. **Use HTTPS for EC2** if possible (recommended for production)
2. **Update Security Group** to only allow necessary ports
3. **Use Environment Variables** for sensitive URLs
4. **Rotate Secrets** regularly

## Custom Domain Setup

If using a custom domain:

1. Update CORS to include your custom domain
2. Update GitHub Pages custom domain in repository settings
3. Update DNS records as instructed by GitHub
4. Update `VITE_API_BASE_URL` if backend also uses custom domain
