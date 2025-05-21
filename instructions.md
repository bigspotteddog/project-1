# Instructions: Modifying the Landing Page Form to Use Fetch API

## Overview
These instructions will guide you through modifying the landing page to submit the interest_list form to the server using the Fetch API with JSON format instead of traditional form submission.

## Steps

1. **Locate the Form in the Landing Page**
   - Open the landing page HTML file (likely `src/main/resources/static/index.html`)
   - Find the form with id="interest_list" or similar identifier

2. **Prevent Default Form Submission**
   - Add an event listener to the form that prevents the default submission
   - This allows us to handle the submission with JavaScript instead

3. **Collect Form Data**
   - When the form is submitted, collect all the form field values
   - Create a JavaScript object with these values

4. **Convert to JSON and Send with Fetch API**
   - Convert the JavaScript object to JSON using `JSON.stringify()`
   - Use the Fetch API to send a POST request to your server endpoint
   - Set the appropriate headers for JSON content

5. **Handle the Server Response**
   - Add code to process the server's response
   - Display success or error messages to the user

## Example Code

```javascript
// Add this to your JavaScript file or in a <script> tag

document.getElementById('interest_list').addEventListener('submit', function(event) {
  // Prevent the form from submitting normally
  event.preventDefault();
  
  // Collect form data
  const formData = {
    name: document.getElementById('name').value,
    email: document.getElementById('email').value,
    // Add other form fields as needed
  };
  
  // Send data using Fetch API
  fetch('/api/interest', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(formData)
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    // Handle success
    console.log('Success:', data);
    // Display success message to user
    document.getElementById('form-response').textContent = 'Thank you for your interest!';
  })
  .catch(error => {
    // Handle error
    console.error('Error:', error);
    // Display error message to user
    document.getElementById('form-response').textContent = 'There was a problem submitting your form.';
  });
});
```

## Server-Side Considerations

1. Ensure your server endpoint is configured to accept JSON requests
2. Add appropriate CORS headers if your frontend and backend are on different domains
3. Validate the incoming JSON data on the server side
4. Return appropriate JSON responses with status codes

## Testing

1. Open the browser console to monitor network requests and any errors
2. Submit the form and verify the request is sent as JSON
3. Check that the server receives and processes the data correctly
