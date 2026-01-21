#!/bin/bash
# Script to build frontend for GitHub Pages with EC2 backend

set -e

echo "=========================================="
echo "Building Frontend for GitHub Pages"
echo "=========================================="

# Check if EC2 URL is provided
if [ -z "$1" ]; then
    echo "Usage: $0 <EC2_BACKEND_URL>"
    echo ""
    echo "Example:"
    echo "  $0 http://54.123.45.67:8080/api"
    echo "  $0 https://api.yourdomain.com/api"
    echo ""
    read -p "Enter your EC2 backend URL (e.g., http://54.123.45.67:8080/api): " EC2_URL
else
    EC2_URL="$1"
fi

# Validate URL format
if [[ ! $EC2_URL =~ ^https?:// ]]; then
    echo "Error: URL must start with http:// or https://"
    exit 1
fi

echo ""
echo "Building with EC2 backend URL: $EC2_URL"
echo ""

# Build with the EC2 URL
VITE_API_BASE_URL="$EC2_URL" npm run build

echo ""
echo "✅ Build complete!"
echo ""
echo "To deploy to GitHub Pages:"
echo "  npm run deploy"
echo ""
echo "Or commit and push to trigger GitHub Actions:"
echo "  git add ."
echo "  git commit -m 'Build for GitHub Pages'"
echo "  git push origin main"
