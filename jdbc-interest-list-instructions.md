# Instructions: Saving Interest List to Database using JDBC

## Overview
These instructions outline the steps to save interest lists from posts to the `interest_list` table in the database using JDBC when they are received in the post controller.

## Prerequisites
- Java Spring Boot application
- JDBC dependency in your project
- PostgreSQL database (or other compatible database)
- Existing post controller that receives interest list data

## Steps

### 1. Configure Database Connection
Ensure your `application.properties` file has the correct database connection settings:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 2. Create Interest List Table
If not already created, execute the following SQL to create the interest_list table:
```sql
CREATE TABLE IF NOT EXISTS interest_list (
    id SERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    interest_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);
```

### 3. Create a JDBC Repository Class
```java
@Repository
public class InterestListRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public InterestListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void saveInterestList(Long postId, List<String> interests) {
        String sql = "INSERT INTO interest_list (post_id, interest_name) VALUES (?, ?)";
        
        List<Object[]> batchArgs = new ArrayList<>();
        for (String interest : interests) {
            batchArgs.add(new Object[] {postId, interest});
        }
        
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    
    public List<String> getInterestsByPostId(Long postId) {
        String sql = "SELECT interest_name FROM interest_list WHERE post_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, postId);
    }
    
    public void deleteInterestsByPostId(Long postId) {
        String sql = "DELETE FROM interest_list WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
}
```

### 4. Modify Post Controller to Save Interest List
```java
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final InterestListRepository interestListRepository;
    
    public PostController(PostService postService, InterestListRepository interestListRepository) {
        this.postService = postService;
        this.interestListRepository = interestListRepository;
    }
    
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        // Save the post first
        Post savedPost = postService.savePost(postRequest.getPost());
        
        // Then save the interest list using JDBC
        if (postRequest.getInterests() != null && !postRequest.getInterests().isEmpty()) {
            interestListRepository.saveInterestList(savedPost.getId(), postRequest.getInterests());
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        // Update the post
        Post updatedPost = postService.updatePost(id, postRequest.getPost());
        
        // Update interests - first delete existing ones, then add new ones
        interestListRepository.deleteInterestsByPostId(id);
        if (postRequest.getInterests() != null && !postRequest.getInterests().isEmpty()) {
            interestListRepository.saveInterestList(id, postRequest.getInterests());
        }
        
        return ResponseEntity.ok(updatedPost);
    }
}
```

### 5. Create a PostRequest DTO
```java
public class PostRequest {
    private Post post;
    private List<String> interests;
    
    // Getters and setters
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public List<String> getInterests() {
        return interests;
    }
    
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
```

### 6. Add JDBC Dependency
If not already in your pom.xml, add:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

### 7. Testing
Test your implementation by sending a POST request to your endpoint with a payload containing both post data and an interest list.

Example request body:
```json
{
  "post": {
    "title": "Sample Post",
    "content": "This is a sample post content"
  },
  "interests": ["Technology", "Programming", "Java", "JDBC"]
}
```

## Troubleshooting
- Ensure your database connection is properly configured
- Check for SQL syntax errors in your queries
- Verify that the post_id foreign key constraint is satisfied
- Use proper exception handling in your repository methods

## Best Practices
- Consider using transactions to ensure atomicity when saving both post and interests
- Implement proper validation for the interest list data
- Add appropriate logging for debugging purposes
- Consider using prepared statements for better security against SQL injection
