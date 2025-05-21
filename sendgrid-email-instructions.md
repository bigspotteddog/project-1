# Using SendGrid to Send Thank You Emails

This document outlines the steps to implement an automated thank you email using SendGrid when users submit their information to the interest list.

## Prerequisites

1. A SendGrid account (sign up at [SendGrid](https://sendgrid.com/))
2. SendGrid API key
3. Spring Boot application with form submission functionality

## Implementation Steps

### 1. Add SendGrid Dependency

Add the SendGrid dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.sendgrid</groupId>
    <artifactId>sendgrid-java</artifactId>
    <version>4.9.3</version>
</dependency>
```

### 2. Configure SendGrid API Key

Add your SendGrid API key to `application.properties`:

```properties
sendgrid.api-key=YOUR_SENDGRID_API_KEY
```

### 3. Create an Email Service

Create a new service class to handle email sending:

```java
package com.example.demo.service;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;
    
    private static final String FROM_EMAIL = "your-email@example.com";
    private static final String EMAIL_SUBJECT = "Thank You for Your Interest!";
    
    public void sendThankYouEmail(String toEmail, String userName) throws IOException {
        Email from = new Email(FROM_EMAIL);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Dear " + userName + ",\n\nThank you for your interest! We've received your information and will be in touch soon.\n\nBest regards,\nThe Team");
        
        Mail mail = new Mail(from, EMAIL_SUBJECT, to, content);
        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        
        Response response = sg.api(request);
        System.out.println("Email sent with status code: " + response.getStatusCode());
    }
}
```

### 4. Integrate with Form Submission Handler

Update your controller to call the email service when a user submits the interest form:

```java
@Autowired
private EmailService emailService;

@PostMapping("/submit-interest")
public String handleInterestFormSubmission(@RequestParam String email, @RequestParam String name, Model model) {
    // Save user information to database
    
    // Send thank you email
    try {
        emailService.sendThankYouEmail(email, name);
    } catch (IOException e) {
        // Log error
        System.err.println("Failed to send email: " + e.getMessage());
    }
    
    // Return success page
    return "thank-you";
}
```

### 5. Test the Implementation

1. Run your Spring Boot application
2. Submit a test form with a valid email address
3. Check that the thank you email is received
4. Monitor SendGrid dashboard for delivery statistics

### 6. Production Considerations

- Use environment variables for API keys instead of hardcoding them
- Implement email templates in SendGrid for better design
- Add error handling and retry logic for failed email attempts
- Consider using asynchronous processing for email sending to avoid blocking user requests

## Troubleshooting

- Check SendGrid logs for delivery issues
- Verify your SendGrid account is active and in good standing
- Ensure your sending domain is verified in SendGrid
- Check spam folders if emails aren't being received
