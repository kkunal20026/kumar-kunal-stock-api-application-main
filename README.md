# Stock API Application

**A Spring Boot RESTful Microservice for Stock CRUD Operations with Docker Deployment**

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](LICENSE)

---

## Table of Contents

1. [Overview](#overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Architecture](#architecture)
5. [Entity Model](#entity-model)
6. [API Endpoints](#api-endpoints)
7. [Configuration](#configuration)
8. [Getting Started](#getting-started)
9. [Docker Deployment](#docker-deployment)
10. [Testing](#testing)
11. [API Documentation (Swagger)](#api-documentation-swagger)
12. [Postman Collection](#postman-collection)
13. [Database Schema](#database-schema)
14. [Logging](#logging)
15. [License](#license)

---

## Overview

The **Stock API Application** is a production-ready Spring Boot microservice that provides full **CRUD** (Create, Read, Update, Delete) operations for managing stock records. The application is built following industry best practices, including:

- **Naming conventions** for packages, classes, and API endpoints
- **Error handling** with structured exception management
- **Logging** using *SLF4J* with *Logback*
- **Pagination and sorting** for large datasets
- **Validation** using Jakarta Bean Validation
- **API documentation** with OpenAPI 3.0 (Swagger UI)
- **Containerization** with Docker and Docker Compose

---

## Technology Stack

| Category              | Technology                                                        |
|-----------------------|-------------------------------------------------------------------|
| **Language**          | Java 21                                                           |
| **Framework**         | Spring Boot 4.0.5 (Spring Framework 7.x, Hibernate 7.x)          |
| **Build Tool**        | Apache Maven 3.x                                                 |
| **Database**          | MySQL 8.x (primary), H2 (in-memory alternative)                  |
| **ORM**               | Spring Data JPA with Hibernate                                   |
| **JDBC Driver**       | MySQL Connector/J (`com.mysql:mysql-connector-j`)                 |
| **API Documentation** | SpringDoc OpenAPI 3.0.3 (Swagger UI)                              |
| **Code Generation**   | Lombok                                                            |
| **Validation**        | Jakarta Bean Validation (`spring-boot-starter-validation`)        |
| **Utilities**         | Apache Commons Lang 3                                             |
| **HATEOAS**           | Spring HATEOAS                                                    |
| **Containerization**  | Docker, Docker Compose                                            |
| **Testing**           | JUnit 5, Mockito, MockMvc, AssertJ                               |
| **Logging**           | SLF4J + Logback                                                   |
| **License**           | Apache License 2.0                                                |

---

## Project Structure

```
kumar-kunal-stock-api-application-main/
├── src/
│   ├── main/
│   │   ├── java/com/kunal/stock/api/
│   │   │   ├── StockApiApplication.java          # Application entry point
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java             # Swagger/OpenAPI configuration
│   │   │   ├── controller/
│   │   │   │   └── StockController.java           # REST controller (all endpoints)
│   │   │   ├── entity/
│   │   │   │   └── Stock.java                     # JPA entity model
│   │   │   ├── exception/
│   │   │   │   └── ResourceNotFoundException.java # Custom exception class
│   │   │   └── repository/
│   │   │       └── StockRepository.java           # Spring Data JPA repository
│   │   └── resources/
│   │       ├── application.properties             # Default configuration
│   │       ├── application-docker.properties      # Docker profile configuration
│   │       ├── config/
│   │       │   └── logback-spring.xml             # Logback logging configuration
│   │       ├── database/
│   │       │   └── schema.sql                     # MySQL database schema & seed data
│   │       └── Stock.postman_collection.json      # Postman API test collection
│   └── test/
│       └── java/com/kunal/stock/api/
│           ├── StockApiApplicationTests.java      # Application context load test
│           ├── config/
│           │   └── OpenApiConfigTest.java          # OpenAPI config unit test
│           ├── controller/
│           │   └── StockControllerTest.java        # Controller unit tests (MockMvc)
│           └── entity/
│               └── StockTest.java                 # Entity unit tests
├── pom.xml                                        # Maven build configuration
├── Dockerfile                                     # Docker image definition
├── docker-compose.yml                             # Docker Compose orchestration
├── LICENSE                                        # Apache License 2.0
├── .gitignore                                     # Git ignore rules
└── README.md                                      # This file
```

---

## Architecture

The application follows a **layered architecture** pattern:

```
┌──────────────────────────────────────────────┐
│                  Client                      │
│         (Postman / Browser / cURL)           │
└──────────────────┬───────────────────────────┘
                   │ HTTP (REST)
┌──────────────────▼───────────────────────────┐
│            StockController                   │
│         (@RestController, /api)              │
│   Handles HTTP requests, error handling,     │
│   logging, and response formatting           │
└──────────────────┬───────────────────────────┘
                   │
┌──────────────────▼───────────────────────────┐
│            StockRepository                   │
│     (JpaRepository<Stock, Integer>)          │
│   CRUD operations, custom queries,           │
│   pagination support                         │
└──────────────────┬───────────────────────────┘
                   │ JPA / Hibernate
┌──────────────────▼───────────────────────────┐
│          MySQL / H2 Database                 │
│            Table: stock                      │
└──────────────────────────────────────────────┘
```

---

## Entity Model

### Stock Entity

The `Stock` entity is mapped to the `stock` table and uses **Jakarta Persistence** annotations with **Lombok** for boilerplate reduction.

| Field          | Type           | Column          | Constraints                          |
|----------------|----------------|-----------------|--------------------------------------|
| `id`           | `Integer`      | `id`            | Primary Key, Auto-generated (IDENTITY) |
| `name`         | `String`       | `name`          | `@NotBlank`, max 20 characters       |
| `currentPrice` | `BigDecimal`   | `currentPrice`  | Stock's current price                |
| `lastUpdate`   | `Timestamp`    | `lastUpdate`    | Auto-set via `@CreationTimestamp`, not updatable |

**Lifecycle Hooks:**

- `@PrePersist` / `@PreUpdate` — Automatically sets `lastUpdate` to the current timestamp when the entity is persisted or updated (if not already set).

**Source:** `src/main/java/com/kunal/stock/api/entity/Stock.java`

```java
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Size(min = 0, max = 20)
    @Column(name = "name")
    private String name;

    @Column(name = "currentPrice")
    private BigDecimal currentPrice;

    @CreationTimestamp
    @Column(name = "lastUpdate", nullable = false, updatable = false)
    private Timestamp lastUpdate;
}
```

---

## API Endpoints

**Base URL:** `/api`

| Method     | Endpoint                        | Description                        | Request Body | Response Code     |
|------------|----------------------------------|------------------------------------|--------------|-------------------|
| `POST`     | `/api/stocks`                   | Create a new stock                 | `Stock` JSON | `201 Created`     |
| `GET`      | `/api/stocks`                   | Retrieve all stocks                | -            | `200 OK`          |
| `GET`      | `/api/stocks/{id}`              | Retrieve a stock by ID             | -            | `200 OK` / `404`  |
| `GET`      | `/api/stocks/page`              | Retrieve stocks with pagination    | -            | `200 OK`          |
| `GET`      | `/api/stocks/page/{page}`       | Retrieve stocks by page number     | -            | `200 OK`          |
| `GET`      | `/api/stocks/name`              | Retrieve distinct stock names      | -            | `200 OK`          |
| `PATCH`    | `/api/stocks/{id}/{currentPrice}` | Update stock price               | -            | `202 Accepted`    |
| `DELETE`   | `/api/stocks/{id}`              | Delete a stock by ID               | -            | `200 OK`          |

### Request & Response Examples

#### 1. Create a Stock

```bash
curl -X POST http://localhost:8080/api/stocks \
  -H "Content-Type: application/json" \
  -d '{
    "name": "AAPL",
    "currentPrice": 185.50
  }'
```

**Response** (`201 Created`):

```json
{
  "id": 1,
  "name": "AAPL",
  "currentPrice": 185.50,
  "lastUpdate": "2026-04-23T10:30:00.000+00:00"
}
```

#### 2. Get All Stocks

```bash
curl -X GET http://localhost:8080/api/stocks
```

**Response** (`200 OK`):

```json
[
  {
    "id": 1,
    "name": "Kumar",
    "currentPrice": 12.00,
    "lastUpdate": "2026-04-23T10:00:00.000+00:00"
  },
  {
    "id": 2,
    "name": "Kunal",
    "currentPrice": 13.00,
    "lastUpdate": "2026-04-23T10:00:00.000+00:00"
  }
]
```

#### 3. Get Stock by ID

```bash
curl -X GET http://localhost:8080/api/stocks/1
```

#### 4. Paginated Stocks

```bash
curl -X GET "http://localhost:8080/api/stocks/page?page=0&size=5&sort=name,desc"
```

#### 5. Update Stock Price

```bash
curl -X PATCH http://localhost:8080/api/stocks/1/199.99
```

#### 6. Delete a Stock

```bash
curl -X DELETE http://localhost:8080/api/stocks/1
```

---

## Configuration

### Default Profile (`application.properties`)

| Property                                        | Value                                                        |
|-------------------------------------------------|--------------------------------------------------------------|
| `server.port`                                   | `8081`                                                       |
| `spring.application.name`                       | `api`                                                        |
| `spring.datasource.url`                         | `jdbc:mysql://localhost:3306/stocks?useUnicode=true&characterEncoding=utf8` |
| `spring.datasource.username`                    | `root`                                                       |
| `spring.jpa.properties.hibernate.dialect`       | `org.hibernate.dialect.MySQLDialect`                         |
| `spring.jpa.hibernate.ddl-auto`                 | `update`                                                     |
| `spring.jpa.show-sql`                           | `true`                                                       |
| `spring.data.web.pageable.default-page-size`    | `5`                                                          |
| `spring.data.web.pageable.max-page-size`        | `2000`                                                       |
| `spring.profiles.active`                        | `docker`                                                     |

### Docker Profile (`application-docker.properties`)

| Property                                        | Value                                                        |
|-------------------------------------------------|--------------------------------------------------------------|
| `server.port`                                   | `8080`                                                       |
| `spring.datasource.url`                         | `jdbc:mysql://stocks-mysqldb:3306/stocks`                    |
| `spring.datasource.username`                    | `root`                                                       |
| `spring.datasource.password`                    | `root`                                                       |
| `spring.sql.init.mode`                          | `always`                                                     |

### Switching to H2 (In-Memory Database)

To use the **H2** database instead of MySQL, uncomment the H2 properties and comment out the MySQL properties in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

The H2 console is enabled by default and accessible at:
> **http://localhost:8081/h2-console**

---

## Getting Started

### Prerequisites

- **Java 21** (JDK) - [Download](https://adoptium.net/temurin/releases/?version=21)
- **Apache Maven 3.x** - [Download](https://maven.apache.org/download.cgi)
- **MySQL 8.x** (or use H2 for development) - [Download](https://dev.mysql.com/downloads/)
- **Docker Desktop** (optional, for containerized deployment) - [Download](https://docs.docker.com/desktop/)

### Step 1: Clone the Repository

```bash
git clone https://github.com/kumar-kunal/kumar-kunal-stock-api-application.git
cd kumar-kunal-stock-api-application-main
```

### Step 2: Set Up the MySQL Database

Run the schema script to create the database, table, and seed data:

```bash
mysql -u root -p < src/main/resources/database/schema.sql
```

This creates:
- Database: `stocks`
- Table: `stock` (columns: `id`, `name`, `current_price`, `last_update`)
- Sample data: 4 stock records (Kumar, Kunal, Jerome, Oskam)

### Step 3: Update Database Credentials

Edit `src/main/resources/application.properties` and set your MySQL credentials:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 4: Build the Project

```bash
mvn clean package
```

> **Note:** Tests are skipped by default (`tests.skip=true`). To run tests during build:
> ```bash
> mvn clean package -Dtests.skip=false
> ```

### Step 5: Run the Application

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

The application starts on:
- **Default profile:** `http://localhost:8081`
- **Docker profile:** `http://localhost:8080`

---

## Docker Deployment

### Dockerfile

The application is containerized using **Eclipse Temurin JDK 21**:

```dockerfile
FROM eclipse-temurin:21
LABEL mentainer="kkunal2005@gmail.com"
WORKDIR /app
COPY target/api-0.0.1-SNAPSHOT.jar /app/api.jar
ENTRYPOINT ["java", "-jar", "-Xmx2048m", "-XX:MaxMetaspaceSize=512m", "api.jar"]
```

### Docker Compose Services

The `docker-compose.yml` orchestrates two services:

| Service                          | Container Name                     | Description                     |
|----------------------------------|------------------------------------|---------------------------------|
| `stocks-mysqldb`                 | `stock-mysqldb`                    | MySQL database server           |
| `stock-springboot-stock-service` | `stock-springboot-stock-service`   | Spring Boot application         |

### Step-by-Step Docker Deployment

> **Important:** Before Docker deployment, toggle *Skip Test* mode and run `clean` and `package` first.

1. **Pull the MySQL Docker image:**
   ```bash
   docker pull mysql
   ```

2. **Create the Docker network:**
   ```bash
   docker network create springboot-mysql-stocks-network
   ```

3. **Verify the network:**
   ```bash
   docker network ls
   ```

4. **Start the MySQL container:**
   ```bash
   docker run --name stocks-mysqldb \
     --network springboot-mysql-stocks-network \
     -e MYSQL_ROOT_PASSWORD=root \
     -e MYSQL_DATABASE=stocks \
     -d mysql
   ```

5. **Build the Spring Boot Docker image:**
   ```bash
   docker build -t stock-springboot-restful-webservices .
   ```

6. **Run with Docker Compose** (recommended):
   ```bash
   docker-compose up
   ```

7. **Verify the containers are running:**
   ```bash
   docker ps
   ```

8. **View container logs:**
   ```bash
   docker logs -f stock-springboot-stock-service
   ```

9. **Access the MySQL database inside the container:**
   ```bash
   docker exec -it stock-mysqldb bash
   mysql -u root -p
   ```

### Stopping the Application

```bash
docker-compose down
```

---

## Testing

The project includes **4 test classes** covering the controller, entity, configuration, and application context layers.

### Test Classes

| Test Class                | File                                          | Description                              |
|---------------------------|-----------------------------------------------|------------------------------------------|
| `StockApiApplicationTests` | `src/test/.../StockApiApplicationTests.java` | Verifies Spring Boot context loads       |
| `StockControllerTest`     | `src/test/.../controller/StockControllerTest.java` | REST endpoint tests using **MockMvc**    |
| `StockTest`               | `src/test/.../entity/StockTest.java`         | Entity lifecycle, equals, hashCode tests |
| `OpenApiConfigTest`       | `src/test/.../config/OpenApiConfigTest.java`  | OpenAPI bean configuration test          |

### Testing Frameworks & Annotations

- **JUnit 5** (`@Test`, `@BeforeEach`, `@ExtendWith`)
- **Mockito** (`@MockitoBean`, `when()`, `verify()`)
- **MockMvc** (`@WebMvcTest`, `MockHttpServletResponse`)
- **AssertJ** (`assertThat()`)

### Running Tests

```bash
# Run all tests
mvn test -Dtests.skip=false

# Run a specific test class
mvn test -Dtests.skip=false -Dtest=StockControllerTest

# Run tests with verbose output
mvn test -Dtests.skip=false -Dsurefire.useFile=false
```

---

## API Documentation (Swagger)

The application integrates **SpringDoc OpenAPI 3.0.3** for interactive API documentation.

Once the application is running, access the Swagger UI at:

> **http://localhost:8080/swagger-ui/index.html**

The OpenAPI configuration is defined in `OpenApiConfig.java`:

| Property        | Value                                            |
|-----------------|--------------------------------------------------|
| **Title**       | Stock Application API                            |
| **Description** | Stock API Application Documented on OpenApi 3    |
| **Version**     | 1.0                                              |
| **Contact**     | kkunal2005@gmail.com                             |
| **License**     | GNU                                              |

---

## Postman Collection

A pre-configured **Postman collection** is included for testing all API endpoints:

> **Location:** `src/main/resources/Stock.postman_collection.json`

**To import:**
1. Open Postman
2. Click **Import**
3. Select the `Stock.postman_collection.json` file
4. All API endpoints will be available for testing

---

## Database Schema

The database schema is defined in `src/main/resources/database/schema.sql`:

```sql
CREATE DATABASE IF NOT EXISTS `stocks`
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `stocks`;

CREATE TABLE `stock` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `name`          VARCHAR(255) NOT NULL,
  `current_price` DECIMAL(19,2) DEFAULT NULL,
  `last_update`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

### Seed Data

| id | name   | current_price | last_update        |
|----|--------|---------------|--------------------|
| 1  | Kumar  | 12.00         | CURRENT_TIMESTAMP  |
| 2  | Kunal  | 13.00         | CURRENT_TIMESTAMP  |
| 3  | Jerome | 14.00         | CURRENT_TIMESTAMP  |
| 4  | Oskam  | 15.00         | CURRENT_TIMESTAMP  |

---

## Logging

The application uses **SLF4J** with **Logback** for structured logging.

### Logback Configuration (`logback-spring.xml`)

| Logger                     | Level   | Appender |
|----------------------------|---------|----------|
| `com.kunal.stock.api`      | `DEBUG` | Console  |
| Root logger                | `ERROR` | Console  |

### Console Log Pattern

```
%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
```

### Log File

Logs are written to:
> `logs/api.log`

---

## Naming Conventions

| Item                 | Convention                                              |
|----------------------|---------------------------------------------------------|
| **Project Name**     | `kumar-kunal-stock-api-application` (Stock API Application) |
| **Package Name**     | `com.kunal.stock.api`                                   |
| **Maven Artifact**   | `api`                                                   |
| **JAR Name**         | `api-0.0.1-SNAPSHOT.jar`                                |
| **API URL Format**   | `/api/stocks`, `/api/stocks/{id}`                       |

---

## Dependencies (pom.xml)

### Runtime Dependencies

| GroupId                        | ArtifactId                            | Scope     |
|--------------------------------|---------------------------------------|-----------|
| `org.springframework.boot`     | `spring-boot-starter-webmvc`          | compile   |
| `org.springframework.boot`     | `spring-boot-starter-data-jpa`        | compile   |
| `org.springframework.boot`     | `spring-boot-starter-jdbc`            | compile   |
| `org.springframework.boot`     | `spring-boot-starter-hateoas`         | compile   |
| `org.springframework.boot`     | `spring-boot-starter-validation`      | compile   |
| `org.springframework.boot`     | `spring-boot-devtools`                | runtime   |
| `com.mysql`                    | `mysql-connector-j`                   | runtime   |
| `com.h2database`               | `h2`                                  | runtime   |
| `org.projectlombok`            | `lombok`                              | compile   |
| `org.apache.commons`           | `commons-lang3`                       | compile   |
| `org.springdoc`                | `springdoc-openapi-starter-webmvc-ui` | compile   |

### Test Dependencies

| GroupId                        | ArtifactId                            | Scope   |
|--------------------------------|---------------------------------------|---------|
| `org.springframework.boot`     | `spring-boot-starter-test`            | test    |
| `org.springframework.boot`     | `spring-boot-starter-webmvc-test`     | test    |

### Build Plugins

| Plugin                         | Version   | Purpose                              |
|--------------------------------|-----------|--------------------------------------|
| `spring-boot-maven-plugin`     | 4.0.5     | Package as executable JAR            |
| `maven-surefire-plugin`        | 3.5.5     | Test execution (skip by default)     |

---

## Author

**Kumar Kunal**
- Email: kkunal2005@gmail.com

---

## License

This project is licensed under the **Apache License 2.0** - see the [LICENSE](LICENSE) file for details.