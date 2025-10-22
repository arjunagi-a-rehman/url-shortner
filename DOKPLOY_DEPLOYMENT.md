# Dokploy Deployment Guide

## 🚀 Quick Deployment Options

### Option 1: Using Pre-built Docker Image (Fastest)

1. **In Dokploy Dashboard:**
   - Create new application
   - Choose "Docker Image"
   - Image: `abdul12527/urlshortner:s4`
   - Port: `8080`

2. **Environment Variables:**
```bash
Mongo_URI=mongodb://username:password@your-mongo-host:27017/urlshortner
SERVER_PORT=8080
PRODUCTION_DOMAIN=https://your-domain.com
```

### Option 2: Build New Image with Analytics Features

1. **Build and Push New Image:**
```bash
cd url-shortner
./mvnw jib:build
```

2. **In Dokploy:**
   - Use image: `abdul12527/urlshortner:analytics-v1`
   - Same environment variables as above

### Option 3: Docker Compose Deployment (Recommended)

1. **Upload `dokploy-compose.yml` to Dokploy**
2. **Set Environment Variables:**
```bash
MONGO_ROOT_USERNAME=admin
MONGO_ROOT_PASSWORD=your-secure-password
MONGO_DB_NAME=urlshortner
PRODUCTION_DOMAIN=https://your-domain.com
```

3. **Deploy the stack**

### Option 4: Build from Source

1. **In Dokploy:**
   - Create new application
   - Choose "Git Repository"
   - Repository: Your Git repo URL
   - Build context: `url-shortner/`
   - Dockerfile: `Dockerfile`

## 🔧 Environment Variables Reference

### Required Variables:
- `Mongo_URI` - Complete MongoDB connection string
- `SERVER_PORT` - Application port (default: 8080)

### Optional Variables:
- `PRODUCTION_DOMAIN` - Your production domain for CORS
- `LOCAL_DOMAIN` - Local development domain
- `JAVA_OPTS` - JVM options (default: -Xmx512m -Xms256m)

## 🗄️ Database Setup

### Option A: Use Dokploy's MongoDB Service
1. Create MongoDB service in Dokploy
2. Use internal connection string

### Option B: External MongoDB
1. Use MongoDB Atlas or external provider
2. Update connection string accordingly

### Option C: Include MongoDB in Compose (Recommended)
- Uses the `dokploy-compose.yml` with built-in MongoDB
- Includes persistent volume for data
- Automatic networking between services

## 🌐 Domain Configuration

1. **Set up your domain in Dokploy**
2. **Update environment variables:**
   - `PRODUCTION_DOMAIN=https://your-domain.com`
3. **SSL will be handled automatically by Dokploy**

## 📊 Analytics Features

The new deployment includes:
- ✅ Click tracking and analytics
- ✅ Geographical data (works with real IPs)
- ✅ User behavior tracking
- ✅ Real-time statistics
- ✅ Enhanced frontend with analytics dashboard

## 🔍 Health Checks

The application includes:
- Health endpoint: `/actuator/health`
- Built-in Docker health checks
- Monitoring-ready with metrics endpoint

## 🚨 Troubleshooting

### Common Issues:

1. **MongoDB Connection Failed:**
   - Check `Mongo_URI` format
   - Ensure MongoDB is accessible
   - Verify credentials

2. **Application Won't Start:**
   - Check logs in Dokploy dashboard
   - Verify environment variables
   - Ensure sufficient memory allocation

3. **Analytics Not Working:**
   - Check if using new image with analytics
   - Verify MongoDB collections are created
   - Check application logs for errors

### Logs Access:
- View logs in Dokploy dashboard
- Use `docker logs url_shortner_app` if using compose

## 📈 Monitoring

Access these endpoints for monitoring:
- Health: `https://your-domain.com/actuator/health`
- Metrics: `https://your-domain.com/actuator/metrics`
- Info: `https://your-domain.com/actuator/info`

## 🔐 Security Considerations

1. **Change default MongoDB credentials**
2. **Use strong passwords**
3. **Enable MongoDB authentication**
4. **Configure proper firewall rules**
5. **Use HTTPS (handled by Dokploy)**

## 📝 Post-Deployment

1. **Test URL creation and redirection**
2. **Verify analytics are working**
3. **Check geographical data with real traffic**
4. **Monitor application performance**
5. **Set up backup for MongoDB data**