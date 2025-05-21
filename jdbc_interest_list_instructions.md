# Instructions: Saving Interest List Data with JDBC

## Overview
These instructions outline how to save interest list data from POST requests to the `interest_list` table using JDBC in a Spring Boot application.

## Prerequisites
- PostgreSQL database with an `interest_list` table
- Spring Boot application with JDBC dependencies
- Controller set up to receive POST requests

## Steps

### 1. Add JDBC Dependencies
Ensure your `pom.xml` includes the JDBC dependencies:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Create a Model Class
Create a model class to represent the interest list data:
```java
public class InterestListEntry {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    
    // Getters and setters
}
```

### 3. Create a Repository Class
Create a repository class to handle database operations:
```java
@Repository
public class InterestListRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public InterestListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void save(InterestListEntry entry) {
        String sql = "INSERT INTO interest_list (email, name, created_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, 
            entry.getEmail(), 
            entry.getName(), 
            entry.getCreatedAt() != null ? entry.getCreatedAt() : LocalDateTime.now()
        );
    }
}
```

### 4. Update the Controller
Modify your controller to use the repository:
```java
@RestController
@RequestMapping("/api")
public class InterestListController {

    private final InterestListRepository repository;
    
    public InterestListController(InterestListRepository repository) {
        this.repository = repository;
    }
    
    @PostMapping("/interest")
    public ResponseEntity<String> addToInterestList(@RequestBody InterestListEntry entry) {
        try {
            repository.save(entry);
            return ResponseEntity.ok("Successfully added to interest list");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to add to interest list: " + e.getMessage());
        }
    }
}
```

### 5. Test the Implementation
Send a POST request to your endpoint:
```
POST /api/interest
Content-Type: application/json

{
  "email": "user@example.com",
  "name": "John Doe"
}
```

### 6. Error Handling and Validation
Add validation to ensure required fields are present:
```java
@PostMapping("/interest")
public ResponseEntity<String> addToInterestList(@RequestBody @Valid InterestListEntry entry) {
    // Implementation
}
```

### 7. Logging
Add logging to track operations:
```java
private static final Logger logger = LoggerFactory.getLogger(InterestListController.class);

@PostMapping("/interest")
public ResponseEntity<String> addToInterestList(@RequestBody InterestListEntry entry) {
    logger.info("Received interest list submission: {}", entry.getEmail());
    // Rest of implementation
}
```

## Troubleshooting
- Verify database connection settings in `application.properties`
- Check table schema matches the SQL in the repository
- Ensure proper error handling for duplicate entries
