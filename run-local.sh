#!/bin/bash

echo "🚀 Building and running URL Shortener locally with Docker..."
echo "📍 Port: 9091"
echo "🗄️  Database: MongoDB Atlas"
echo ""

# Stop any existing containers
echo "🛑 Stopping existing containers..."
docker-compose -f docker-compose-local.yml down

# Build and start the application
echo "🔨 Building and starting application..."
docker-compose -f docker-compose-local.yml up --build

echo ""
echo "✅ Application should be running at:"
echo "   🌐 Frontend: http://localhost:9091"
echo "   📊 Health Check: http://localhost:9091/actuator/health"
echo "   📚 API Docs: http://localhost:9091/swagger-ui.html"
echo ""
echo "🛑 To stop: docker-compose -f docker-compose-local.yml down"