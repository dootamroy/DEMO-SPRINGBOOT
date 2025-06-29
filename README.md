# DEMO-SPRINGBOOT

## Tech Stack Used

- **Java 17**: Programming language for all services
- **Spring Boot 3.2.x**: Backend framework for microservices
- **Spring Cloud Netflix Eureka**: Service discovery (Eureka server and clients)
- **Spring Data JPA**: ORM for database access
- **PostgreSQL**: Relational database
- **Log4j2**: Logging framework
- **Maven**: Build and dependency management
- **Docker**: Containerization of services
- **Docker Compose**: Orchestration of multi-container applications
- **OpenJDK 17 (slim)**: Base image for Docker containers

## Environment Configuration

### Setup Environment Variables

1. Copy the example environment file:
   ```bash
   cp env.example .env
   ```

2. Edit the `.env` file with your specific configuration:
   ```bash
   # Database Configuration
   DB_HOST=your_database_host
   DB_PORT=5432
   DB_NAME=your_database_name
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   
   # Service-specific database names
   DEMO1_DB_NAME=demo1_db
   DEMO2_DB_NAME=demo2_db
   
   # Service-specific JDBC URLs
   DEMO1_DATASOURCE_URL=jdbc:postgresql://your_host:5432/demo1_db
   DEMO2_DATASOURCE_URL=jdbc:postgresql://your_host:5432/demo2_db
   ```

### Environment Variables by Service

#### Eureka Server
- `EUREKA_SERVER_PORT`: Port for Eureka server (default: 8761)
- `EUREKA_SERVER_HOST`: Hostname for Eureka server (default: eureka-server)

#### Demo1 Service
- `DEMO1_SERVICE_PORT`: Service port (default: 8081)
- `DEMO1_DB_NAME`: Database name for demo1 (default: demo1_db)
- `DEMO1_DATASOURCE_URL`: Complete JDBC URL for demo1 service
- Uses shared database credentials (host, port, username, password)

#### Demo2 Service
- `DEMO2_SERVICE_PORT`: Service port (default: 8082)
- `DEMO2_DB_NAME`: Database name for demo2 (default: demo2_db)
- `DEMO2_DATASOURCE_URL`: Complete JDBC URL for demo2 service
- Uses shared database credentials (host, port, username, password)

### Database Configuration

Each service has its own segregated JDBC URL configuration:

#### **Demo1 Service**
- **Database Name**: `${DEMO1_DB_NAME}` (default: demo1_db)
- **JDBC URL**: `${DEMO1_DATASOURCE_URL}`
- **Shared**: Host, port, username, and password

#### **Demo2 Service**
- **Database Name**: `${DEMO2_DB_NAME}` (default: demo2_db)
- **JDBC URL**: `${DEMO2_DATASOURCE_URL}`
- **Shared**: Host, port, username, and password

### JDBC URL Structure

The JDBC URLs are constructed as follows:
```
jdbc:postgresql://${DB_HOST}:${DB_PORT}/${SERVICE_DB_NAME}
```

**Examples:**
- Demo1: `jdbc:postgresql://100.75.215.139:5432/demo1_db`
- Demo2: `jdbc:postgresql://100.75.215.139:5432/demo2_db`

### Security Notes

- The `.env` file is automatically ignored by Git to prevent committing sensitive credentials
- Always use strong passwords and secure database connections in production
- Consider using Docker secrets or Kubernetes secrets for production deployments
- Each service can connect to completely different database instances if needed
 
