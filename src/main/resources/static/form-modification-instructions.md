# Instructions: Modifying the Landing Page Form to Use Fetch API

## Overview
These instructions will guide you through modifying the landing page to submit the signup form to the server using the Fetch API with JSON format instead of traditional form submission.

## Steps

1. **Identify the Form in the Landing Page**
   - The form is located in the "Sign Up Form" section of index.html
   - It currently has no ID and uses default form submission

2. **Add Form ID and Prevent Default Submission**
   - Add an ID to the form element: `id="interestForm"`
   - Add JavaScript to prevent default form submission

3. **Collect Form Data**
   - When the form is submitted, collect all form field values:
     - First Name (`firstName`)
     - Last Name (`lastName`)
     - Email Address (`email`)
     - Phone Number (`phone`)
     - Text message preference checkbox (`gridCheck`)

4. **Send Data with Fetch API**
   - Convert the collected data to JSON
   - Use Fetch API to POST the data to the server endpoint
   - Set appropriate headers for JSON content

5. **Handle the Response**
   - Process the server's response
   - Show appropriate feedback to the user

## Implementation Code

Add this JavaScript code before the closing `</body>` tag:

```javascript
document.addEventListener('DOMContentLoaded', function() {
    // Get the form element
    const form = document.querySelector('#signup form');
    form.id = 'interestForm';
    
    // Add submit event listener
    form.addEventListener('submit', function(event) {
        // Prevent default form submission
        event.preventDefault();
        
        // Collect form data
        const formData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            receiveTextUpdates: document.getElementById('gridCheck').checked
        };
        
        // Send data using Fetch API
        fetch('/api/interest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
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
            
            // Show success message
            const formSection = document.querySelector('.form-section');
            formSection.innerHTML = '<h2 class="text-center mb-4">Thank You!</h2>' +
                '<p class="text-center">We\'ve received your information and will keep you updated about our grand opening!</p>';
        })
        .catch(error => {
            // Handle error
            console.error('Error:', error);
            
            // Show error message
            const submitBtn = form.querySelector('button[type="submit"]');
            const errorMsg = document.createElement('div');
            errorMsg.className = 'alert alert-danger mt-3';
            errorMsg.textContent = 'There was a problem submitting your information. Please try again.';
            submitBtn.parentNode.appendChild(errorMsg);
        });
    });
});
```

## Server-Side Requirements

1. Create a REST controller to handle the POST request at `/api/interest`
2. Configure the endpoint to accept and process JSON data
3. Implement validation for the submitted data
4. Return appropriate responses:
   - 200 OK with JSON response for successful submissions
   - Appropriate error codes for validation failures or server errors

## Testing

1. Open the landing page in a browser
2. Fill out the form and submit it
3. Check the browser's developer console (F12) to verify:
   - The form submission is intercepted
   - The JSON data is correctly formatted
   - The fetch request is sent to the server
4. Verify the server receives and processes the data correctly
