# Analytics Features Documentation

## Overview
The URL shortener now includes comprehensive analytics tracking to provide insights into URL usage patterns, user behavior, and geographical distribution of clicks.

## New Features Added

### 1. Click Tracking
- **Total Clicks**: Tracks every click on a short URL
- **Unique Users**: Tracks unique sessions to identify distinct users
- **Last Click Time**: Records when the URL was last accessed

### 2. Geographical Analytics
- **Country Tracking**: Identifies the country of origin for each click
- **Region/State Tracking**: Provides regional breakdown within countries
- **City Tracking**: Detailed city-level location data
- **Timezone Information**: Records the timezone of the click origin

### 3. User Behavior Analytics
- **Referrer Tracking**: Captures the source website that led to the click
- **User Agent Tracking**: Records browser and device information
- **Session Management**: Uses HTTP sessions to identify unique users
- **Click History**: Maintains detailed logs of all click activities

### 4. Enhanced API Endpoints

#### New Analytics Endpoint
```
GET /api/analytics/{shortUrlCode}
```
Returns comprehensive analytics including:
- Total and unique click counts
- Geographical distribution (countries and regions)
- Recent click activities (last 10 clicks)
- Daily click statistics (last 30 days)

#### Enhanced Details Endpoint
The existing `/api/details/{shortUrlCode}` now includes:
- `totalClicks`: Total number of clicks
- `uniqueClicks`: Number of unique users
- `lastClickedAt`: Timestamp of last click

#### Enhanced Redirect Endpoint
The `/s/{shortUrlCode}` endpoint now:
- Tracks analytics for each click
- Records IP address, user agent, and referrer
- Generates unique session IDs for user tracking
- Performs GeoIP lookup for location data

### 5. Frontend Enhancements

#### Analytics Dashboard
- **Quick Stats**: Displays total clicks, unique users, and last click time
- **Detailed Analytics Button**: Provides access to comprehensive analytics
- **Geographical Visualization**: Shows top countries and regions
- **Recent Activity**: Displays recent click activities with location and time
- **Performance Metrics**: Shows unique click rate and other KPIs

#### Improved UI Components
- **Stat Cards**: Visual representation of key metrics
- **Analytics Cards**: Organized display of geographical and activity data
- **Responsive Design**: Mobile-friendly analytics display

### 6. Technical Implementation

#### New Models
- `ClickAnalytics`: Stores individual click records with full context
- Enhanced `Url` model with analytics fields

#### New Services
- `IAnalyticsService`: Interface for analytics operations
- `DefaultAnalyticsService`: Implementation with async click recording
- `GeoLocationService`: IP-to-location conversion using ip-api.com

#### New Repositories
- `IClickAnalyticsRepo`: MongoDB repository for click data with custom queries

### 7. Data Privacy & Performance

#### Privacy Considerations
- IP addresses are used only for geographical analysis
- No personally identifiable information is stored
- Session IDs are temporary and not linked to user accounts

#### Performance Optimizations
- Async click recording to avoid redirect delays
- Efficient MongoDB queries with proper indexing
- Caching of geographical data for common IPs
- Graceful fallback for failed GeoIP lookups

### 8. Usage Examples

#### Getting Basic URL Details
```javascript
fetch('/api/details/abc123')
  .then(response => response.json())
  .then(data => {
    console.log('Total clicks:', data.totalClicks);
    console.log('Unique users:', data.uniqueClicks);
  });
```

#### Getting Comprehensive Analytics
```javascript
fetch('/api/analytics/abc123')
  .then(response => response.json())
  .then(data => {
    console.log('Top countries:', data.clicksByCountry);
    console.log('Recent activity:', data.recentClicks);
    console.log('Daily stats:', data.dailyClickStats);
  });
```

### 9. Future Enhancements
- Real-time analytics dashboard
- Export functionality for analytics data
- Advanced filtering and date range selection
- Bot detection and filtering
- Custom analytics events
- A/B testing capabilities

## API Response Examples

### Enhanced URL Details Response
```json
{
  "originalUrl": "https://example.com",
  "shortUrlCode": "abc123",
  "expiryDate": "2024-01-08T10:30:00",
  "createdAt": "2024-01-07T10:30:00",
  "totalClicks": 42,
  "uniqueClicks": 28,
  "lastClickedAt": "2024-01-07T15:45:30"
}
```

### Analytics Response
```json
{
  "shortUrlCode": "abc123",
  "originalUrl": "https://example.com",
  "totalClicks": 42,
  "uniqueClicks": 28,
  "createdAt": "2024-01-07T10:30:00",
  "expiryDate": "2024-01-08T10:30:00",
  "lastClickedAt": "2024-01-07T15:45:30",
  "clicksByCountry": {
    "United States": 15,
    "India": 12,
    "United Kingdom": 8,
    "Canada": 5,
    "Germany": 2
  },
  "clicksByRegion": {
    "United States - California": 8,
    "India - Maharashtra": 7,
    "United Kingdom - England": 6
  },
  "recentClicks": [
    {
      "country": "United States",
      "region": "California",
      "city": "San Francisco",
      "clickedAt": "2024-01-07T15:45:30",
      "referrer": "https://google.com",
      "userAgent": "Mozilla/5.0..."
    }
  ],
  "dailyClickStats": {
    "2024-01-07": 15,
    "2024-01-06": 12,
    "2024-01-05": 8
  }
}
```