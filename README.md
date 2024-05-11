#User Registration and Authentication API
This API provides functionality for user registration, authentication, and profile management. It also includes integration with a third-party API.

Features
User registration with basic validation and secure API
User authentication with basic validation and secure API
User profile management
Integration with a third-party API
Logging of API calls to the third-party API along with user ID and timestamp

API Endpoints

Sign Up User
Endpoint: http://localhost:8089/api/v1/users/signup
Method: POST
Description: Register a new user
Request Body:
First Name (String, required)
Last Name (String, required)
Email (String, required, unique)
Mobile (String, required)
Username (String, required, alphanumeric, 4-15 characters)
Password (String, required, 8-15 characters with at least 1 upper, 1 lower, 1 digit, and 1 special character)

Login User
Endpoint: http://localhost:8089/api/v1/users/login
Method: POST
Description: Authenticate a user
Request Body:
Username (String, required)
Password (String, required)

Update User
Endpoint: http://localhost:8089/api/v1/users/update
Method: PUT
Description: Update user details (requires authentication)
Request Body:
First Name (String)
Last Name (String)
Mobile (String)
Password (String)
Third-party API Integration

Endpoint: http://localhost:8089/crypto-data/by-user
Method: POST
Description: Create an API call to a third-party API
Request Body:
Third-party API request parameters
Response: Store the request response in the database along with user ID and timestamp

Validation Provided
Username must be alphanumeric and 4-15 characters long
Password must be 8-15 characters long and contain at least 1 upper case letter, 1 lower case letter, 1 digit, and 1 special character
Security
API requests must include predefined header values to confirm their origin
Only logged-in users can update their details
Technologies Used
Java
Spring Boot
MySQL
Maven
