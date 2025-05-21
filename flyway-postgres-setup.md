# Setting Up Flyway with PostgreSQL

This guide provides step-by-step instructions for setting up Flyway to maintain a PostgreSQL database running on localhost.

## Prerequisites

- Java 21
- Maven
- PostgreSQL installed and running on localhost:5432

## Step 1: Add Flyway Dependencies to pom.xml

Add the following dependencies to your `pom.xml` file:

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
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=jcava
spring.datasource.password=password

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

## Step 3: Create Migration Directory Structure

Create the following directory structure for your SQL migration files:

```
src/main/resources/db/migration/
```

## Step 4: Create Sample SQL Migration Files

### V1__Create_users_table.sql

Create your first migration file to create a users table:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

## Step 5: Run the Application

When you run your Spring Boot application, Flyway will automatically:
1. Detect the migration files
2. Create a flyway_schema_history table to track migrations
3. Execute the SQL scripts in order

## Step 6: Verify the Migration

Connect to your PostgreSQL database and verify that:
1. The users table has been created
2. Sample data has been inserted
3. The flyway_schema_history table contains entries for your migrations

## Additional Tips

- Always name your migration files using the pattern: `V{version}__{description}.sql`
- Never modify existing migration files after they've been applied
- For new changes, create new migration files with incremented version numbers
- You can use `R__` prefix for repeatable migrations that run when their content changes

## Troubleshooting

- If you encounter errors about existing tables, you may need to set `spring.flyway.baseline-on-migrate=true`
- For a clean start, you can drop all tables and let Flyway recreate them
- Check the Flyway documentation for advanced configuration options
