
# URL Shortener Service

This URL Shortener service simplifies the process of generating, managing, and redirecting short URLs. It offers a RESTful API to create short URLs from long ones, retrieve URL details, update URLs, and delete them. The service is built using Spring Boot, MongoDB, and employs Swagger for API documentation.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Exception Handling](#exception-handling)
- [Service Details](#service-details)
- [Docker Configuration](#docker-configuration)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Technologies Used

The URL Shortener Service relies on the following technologies:

- **Spring Boot:** A robust and flexible Java-based framework for building web applications.
- **MongoDB:** A NoSQL database for storing URL records and analytics data.
- **Swagger (OpenAPI):** Used for API documentation, allowing developers to explore and understand the available endpoints.
- **Lombok:** A library to simplify Java code by reducing boilerplate code.
- **Murmur3 32-bit hash:** Used for encoding original URLs to generate short URL codes.
- **Jib Plugin for Docker Image:** Simplifies the process of creating Docker images without a Dockerfile.
- **GeoIP Service:** Provides geographical location data for click analytics.

## New Analytics Features ✨

The service now includes comprehensive analytics tracking:

- **Click Analytics:** Track total clicks, unique users, and click patterns
- **Geographical Insights:** See where your links are being clicked from (country, region, city)
- **User Behavior:** Monitor referrer sources and user agents
- **Real-time Data:** Get up-to-date statistics on URL performance
- **Historical Tracking:** View daily click statistics and trends

For detailed information about analytics features, see [ANALYTICS_FEATURES.md](ANALYTICS_FEATURES.md).

## Getting Started

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/arjunagi-a-rehman/url-shortner.git
   cd url-shortner
   ```

2. **Build and Run the Application:**

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   The application will start on [http://localhost:8080](http://localhost:8080).

3. **Explore Swagger Documentation:**
   Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to interact with the APIs using Swagger.

## API Documentation

### 1. Generate Short URL

**Endpoint:** `POST /url`

- **Description:** Creates a short URL from the provided long URL.
- **Request Body:**
  ```json
  {
    "url": "https://example.com",
    "expiryDate": "2023-12-31T23:59:59"
  }
  ```
- **Response:**
  ```json
  {
    "originalUrl": "https://example.com",
    "shortUrl": "https://sus9.in/abc123",
    "expiryDate": "2023-12-31T23:59:59"
  }
  ```

### 2. Redirect to Original URL

**Endpoint:** `GET /{shortUrlCode}`

- **Description:** Redirects to the original URL associated with the given short URL code.

### 3. Get URL Details

**Endpoint:** `GET /api/details/{shortUrlCode}`

- **Description:** Gets details regarding the short URL code including analytics data.
- **Response:**
  ```json
  {
    "originalUrl": "https://example.com",
    "shortUrlCode": "abc123",
    "expiryDate": "2023-12-31T23:59:59",
    "createdAt": "2023-12-01T10:30:00",
    "totalClicks": 42,
    "uniqueClicks": 28,
    "lastClickedAt": "2023-12-30T15:45:30"
  }
  ```

### 6. Get Analytics

**Endpoint:** `GET /api/analytics/{shortUrlCode}`

- **Description:** Gets comprehensive analytics for a short URL including geographical distribution and click patterns.
- **Response:**
  ```json
  {
    "shortUrlCode": "abc123",
    "originalUrl": "https://example.com",
    "totalClicks": 42,
    "uniqueClicks": 28,
    "clicksByCountry": {
      "United States": 15,
      "India": 12,
      "United Kingdom": 8
    },
    "recentClicks": [
      {
        "country": "United States",
        "region": "California",
        "city": "San Francisco",
        "clickedAt": "2023-12-30T15:45:30"
      }
    ],
    "dailyClickStats": {
      "2023-12-30": 15,
      "2023-12-29": 12
    }
  }
  ```

### 4. Update URL

**Endpoint:** `PUT /url`

- **Description:** Updates the expiry date and/or the original URL for a particular short URL code.
- **Request Parameters:**
  - `shortUrlCode` (query parameter)
- **Request Body:**
  ```json
  {
    "url": "https://new-example.com",
    "expiryDate": "2024-01-05T14:12:13.094Z"
  }
  ```
- **Response:**
  ```json
  {
    "message": "Updated successfully"
  }
  ```

### 5. Delete URL

**Endpoint:** `DELETE /url/{shortUrlCode}`

- **Description:** Deletes the URL record based on the short URL code.
- **Response:**
  ```json
  {
    "message": "Deleted successfully"
  }
  ```

## Project Structure

The project adheres to the standard Spring Boot structure. Key packages include:

- `com.arjunagi.urlshortner.controller`: API controllers
- `com.arjunagi.urlshortner.dtos`: Data Transfer Objects
- `com.arjunagi.urlshortner.exceptions`: Custom exception classes
- `com.arjunagi.urlshortner.mapper`: Object mapping classes
- `com.arjunagi.urlshortner.models`: Entity classes
- `com.arjunagi.urlshortner.repository`: MongoDB repository interfaces
- `com.arjunagi.urlshortner.services`: Service interfaces and implementations

## Exception Handling

The project includes a global exception handler (`GlobalExceptionHandler`) to gracefully handle exceptions and return appropriate HTTP responses. Custom exceptions, such as `ExpiredUrlException` and `ResourceNotFoundException`, represent specific error scenarios.

## Service Details

The main class (`UrlshortnerApplication`) initializes the Spring Boot application and includes Swagger/OpenAPI annotations for API documentation.

The `UrlController` class contains REST endpoints for URL generation, redirection, details retrieval, updating, and deletion. Each endpoint is documented using Swagger annotations.

The `DefaultUrlService` class implements the `IUrlServices` interface, providing methods for URL generation, retrieval, updating, and deletion. It also includes an asynchronous method (`asyncDelete`) for asynchronous URL deletion.

## Docker Configuration

The application uses the Jib plugin for Docker image creation. The `docker-compose.yml` file includes environment variables for MongoDB configuration. Ensure you provide the correct MongoDB host, port, database name, username, and password in the `docker-compose.yml` file.

```yaml
services:
  urlshortner:
    image: "abdul12527/urlshortner:s4"
    container_name: url_shortner
    ports:
      - 8080:8080
    deploy:
      resources:
        limits:
          memory: 700m

    environment:
      - Mongo_Host=mongodb host name
      - Mongo_Port=mongodb port number
      - Mongo_db_name=mongodb db name
      - Mongo_username=mongodb user name
      - Mongo_pass=mongodb password
```

## Deployment

The service is deployed at [https://sus9.in/swagger-ui/index.html#/](https://sus9.in/swagger-ui/index.html#/). The deployment on AWS EC2 involved setting up a reverse proxy using Nginx and obtaining an SSL certificate using Certbot.

## Contributing

Contributions to the project are welcome. Please follow the [contribution guidelines](CONTRIBUTING.md) for details.

## License

This project is licensed under the [MIT License](LICENSE).
