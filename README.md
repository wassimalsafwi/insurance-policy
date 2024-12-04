# Insurance Policy Management Application

This application is a demonstration project for managing insurance policies, developed as part of a job requirement. The project includes a RESTful API for CRUD operations on insurance policies, built using Spring Boot, and tested using Testcontainers for integration tests.

# Features
- RESTful API for managing insurance policies.
- Detailed API documentation available through Swagger.
- Integration tests leveraging Testcontainers for a PostgreSQL database.

# Prerequisites
- Docker and Docker Compose installed.
- Java 23.
- Maven for building the project.

# Start the application
- mvn clean install
- mvn spring-boot:run
- http://localhost:8080/swagger-ui.html


## Running Tests
- Make sure Docker is running.
- Navigate to the root of the project.
- Start the test database with Docker Compose: docker-compose up -d
- run the tests : mvn test