# Weather Information Service

The Weather Information Service is a simple Spring Boot application that provides current weather data for different cities. This service allows users to retrieve weather information for specific cities, update weather data for existing cities, add new cities with their weather details, and delete weather information for cities.

## Features

- Retrieve weather information for a specific city
- Get weather information for all cities

## Technologies Used

- Java
- Spring Boot
- Maven
- Lombok
- Swagger UI
- Actuator

## Usage

### Prerequisites

- Java Development Kit (JDK) installed
- Apache Maven installed
- Git installed (optional)

### Running the Application

1. Clone the repository:

    ```bash
    git clone https://github.com/sunishthankachan/weather-service.git
    ```

2. Navigate to the project directory:

    ```bash
    cd weather-service
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

4. Run the application:

    ```bash
    java -jar target/weather-service.jar
    ```

5. Once the application is running, you can access the Swagger UI to interact with the API:

   [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## API Endpoints

- `GET /api/v1/weather/{city}`: Get weather information for a specific city.
- `GET /api/v1/weather`: Get weather information for all cities.

## Configuration

The application can be configured using the following properties:

### Logging Configuration

- `logging.file.name`: Specifies the name and location of the log file.
    - **Example**: `logs/app.log`

- `logging.pattern.file`: Defines the pattern for log entries in the file.
    - **Example**: `%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n`

### Example `application.properties`

```properties
spring.application.name=weather-service
server.port=8080

# Logging Configuration
logging.file.name=logs/app.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# Open API Configuration
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/api-docs

