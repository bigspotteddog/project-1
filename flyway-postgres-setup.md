# Setting Up Flyway with PostgreSQL

This guide provides step-by-step instructions for setting up Flyway to maintain a PostgreSQL database running on localhost.

## Prerequisites

- Java 8 or higher
- Maven
- PostgreSQL installed and running on localhost:5432

## Step 1: Add Flyway Dependencies to Your Spring Boot Project

Add the following dependencies to your `pom.xml`:

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Step 2: Configure Database Connection in application.properties

Add the following properties to your `application.properties` file:

```properties
# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/demo
spring.datasource.username=jcava
spring.datasource.password=password

# Flyway configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

## Step 3: Create Migration Directory Structure

Create the following directory structure for your SQL migration files:

```
src/main/resources/db/migration/
```

## Step 4: Create Sample Migration Files

### V1__Create_users_table.sql

Create your first migration file to create a users table:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### V2__Insert_sample_users.sql

Create a second migration file to insert sample data:

```sql
INSERT INTO users (username, email) VALUES
('john_doe', 'john@example.com'),
('jane_smith', 'jane@example.com'),
('bob_johnson', 'bob@example.com');
```

## Step 5: Run Your Application

When you start your Spring Boot application, Flyway will automatically:
1. Detect the migration files
2. Create a flyway_schema_history table to track migrations
3. Execute any pending migrations in order

## Step 6: Verify the Migration

Connect to your PostgreSQL database and verify that:
1. The users table was created
2. Sample data was inserted
3. The flyway_schema_history table contains records of your migrations

```sql
SELECT * FROM users;
SELECT * FROM flyway_schema_history;
```

## Additional Tips

- Always use the naming convention `V{version}__{description}.sql` for migration files
- Version numbers should be sequential and can include dots (e.g., V1.1, V1.2)
- Never modify existing migration files after they've been applied
- For new changes, always create new migration files
