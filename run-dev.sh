#!/bin/bash

# Development startup script for URL Shortener Service

echo "🚀 Starting URL Shortener Service in Development Mode..."

# Set Java 17
export JAVA_HOME=/opt/homebrew/opt/openjdk@17

# Set development environment variables
export SPRING_PROFILES_ACTIVE=dev
export Mongo_URI=mongodb://localhost:27017/urlshortner_dev

# Check if MongoDB is running
if ! pgrep -x "mongod" > /dev/null; then
    echo "⚠️  MongoDB is not running. Please start MongoDB first."
    echo "   You can start it with: brew services start mongodb-community"
    exit 1
fi

echo "✅ MongoDB is running"
echo "✅ Java 17 configured"
echo "✅ Development profile active"
echo ""
echo "🌐 Application will be available at: http://localhost:8080"
echo "📚 Swagger UI will be available at: http://localhost:8080/swagger-ui/index.html"
echo ""

# Start the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev