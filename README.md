# Spring Boot Application with Docker and PostgreSQL

This is a Spring Boot 3+ application with PostgreSQL database running in Docker containers.

## Prerequisites

- Docker Desktop installed and running
- Java 17 (only for local development without Docker)
- Maven (only for local development without Docker)

## Quick Start with Docker (Recommended)

### 1. Build and Run with Docker Compose

```powershell
# Build and start both PostgreSQL and Spring Boot containers
docker-compose up --build

# Or run in detached mode (background)
docker-compose up -d --build
```

The application will be available at: **http://localhost:8081**

### 2. Stop the Containers

```powershell
# Stop containers
docker-compose down

# Stop and remove volumes (clears database data)
docker-compose down -v
```

## Configuration

### Database Connection

The application uses environment variables for database configuration:

**For Local Development (without Docker):**
- DB Host: `localhost:5432`
- Database: `mydb`
- Username: `myuser`
- Password: `strong_password`

**For Docker Environment:**
- DB Host: `postgres:5432` (container name)
- Database: `mydb`
- Username: `myuser`
- Password: `strong_password`

Environment variables are configured in `docker-compose.yml` and application detects them automatically.

### Default Users

The application comes with pre-configured users:

- **Admin User**
  - Username: `admin`
  - Password: `admin123`
  - Role: ADMIN

- **Regular User**
  - Username: `user`
  - Password: `user123`
  - Role: USER

## Running Locally (Without Docker)

### 1. Start PostgreSQL Only

```powershell
# Start only PostgreSQL container
docker-compose up postgres
```

### 2. Run Spring Boot Application Locally

```powershell
# Build the application
mvn clean package -DskipTests

# Run the application
java -jar target/assignment-0.0.1-SNAPSHOT.jar
```

Or use Maven:

```powershell
mvn spring-boot:run
```

## Docker Commands Reference

```powershell
# View running containers
docker ps

# View application logs
docker-compose logs app

# View PostgreSQL logs
docker-compose logs postgres

# View logs in real-time
docker-compose logs -f

# Rebuild specific service
docker-compose up -d --build app

# Access PostgreSQL CLI
docker exec -it postgres-db psql -U myuser -d mydb

# Check database tables
docker exec -it postgres-db psql -U myuser -d mydb -c "\dt"
```

## Troubleshooting

### Port Already in Use

If port 8081 or 5432 is already in use:

```powershell
# Find process using port
netstat -ano | findstr :8081
netstat -ano | findstr :5432

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Database Connection Failed

```powershell
# Check if PostgreSQL is healthy
docker-compose ps

# Restart containers
docker-compose restart

# Check PostgreSQL logs
docker-compose logs postgres
```

### Clear Everything and Start Fresh

```powershell
# Stop and remove all containers, networks, and volumes
docker-compose down -v

# Remove all application images
docker rmi assignment-app postgres:15

# Rebuild from scratch
docker-compose up --build
```

## Project Structure

```
assignment/
├── src/
│   ├── main/
│   │   ├── java/com/example/assignment/
│   │   │   ├── config/          # Security configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── repository/      # Data repositories
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       └── application.yaml # Application configuration
│   └── test/
├── Dockerfile                   # Docker image definition
├── docker-compose.yml          # Multi-container setup
├── pom.xml                     # Maven dependencies
└── README.md
```

## Technologies Used

- Spring Boot 3.2.2
- PostgreSQL 15
- Spring Security
- Spring Data JPA
- Hibernate
- Maven
- Docker & Docker Compose
- Java 17

## API Endpoints

- `GET /api/students` - Get all students
- `POST /api/students` - Create student
- `GET /api/teachers` - Get all teachers
- `POST /api/teachers` - Create teacher
- `GET /api/courses` - Get all courses
- `POST /api/courses` - Create course
- `GET /api/departments` - Get all departments
- `POST /api/departments` - Create department

## Notes

- Database tables are automatically created/updated on startup (`ddl-auto=update`)
- PostgreSQL data persists in Docker volume `postgres-data`
- CSRF is disabled for development (enable in production)
- SQL queries are logged in console for debugging
