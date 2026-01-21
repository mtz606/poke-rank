# Frontend Dockerfile
FROM node:20-alpine AS build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build argument for API URL (defaults to local backend if not provided)
ARG VITE_API_BASE_URL=http://localhost:8080/api

# Build the application with the API URL
RUN VITE_API_BASE_URL=${VITE_API_BASE_URL} npm run build -- --base=/

# Production stage with Nginx
FROM nginx:alpine

# Copy built files to nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]

