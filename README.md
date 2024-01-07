# URL Shortener Service

This project provides APIs for creating, updating, and deleting short URLs. The service allows you to convert long and space-consuming URLs into short URLs and provides control over the URL expiry time.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Exception Handling](#exception-handling)
- [Service Details](#service-details)
- [Docker Configuration](#docker-configuration)
- [Contributing](#contributing)
- [License](#license)

## Technologies Used

- Spring Boot
- MongoDB
- Swagger (OpenAPI)
- Lombok
- Murmur3 32-bit hash
- Jib Plugin for Docker Image

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/arjunagi-a-rehman/url-shortner.git
   cd url-shortner
   ```

2. Build and run the application:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   The application will start on [http://localhost:8080](http://localhost:8080).

3. Access Swagger documentation:

   Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore and interact with the APIs using Swagger.

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

**Endpoint:** `GET /details/{shortUrlCode}`

- **Description:** Gets details regarding the short URL code.
- **Response:**
  ```json
  {
    "originalUrl": "https://example.com",
    "shortUrl": "https://sus9.in/abc123",
    "expiryDate": "2023-12-31T23:59:59"
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

The project follows a standard Spring Boot structure. Here are the key packages:

- `com.arjunagi.urlshortner`: Main package
  - `controller`: API controllers
  - `dtos`: Data Transfer Objects
  - `exceptions`: Custom exception classes
  - `mapper`: Object mapping classes
  - `models`: Entity classes
  - `repository`: MongoDB repository interfaces
  - `services`: Service interfaces and implementations

## Exception Handling

The project includes a global exception handler (`GlobalExceptionHandler`) to handle exceptions gracefully and return appropriate HTTP responses. Custom exceptions, such as `ExpiredUrlException` and `ResourceNotFoundException`, are used to represent specific error scenarios.

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

## Contributing

Contributions to the project are welcome. Please follow the [contribution guidelines](CONTRIBUTING.md) for details.

## License

This project is licensed under the [MIT License](LICENSE).
