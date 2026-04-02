# Finance Data Processing & Access Control API

A robust, secure, and logically structured backend system built with Spring Boot for managing financial records. This project was designed to demonstrate core backend engineering principles including Role-Based Access Control (RBAC), Data Transfer Objects (DTOs), clean architecture, and secure RESTful API design.

## 🚀 Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.4.x (Web, Data JPA, Security, Validation)
* **Database:** H2 In-Memory Database (Chosen for seamless setup and evaluation)
* **Security:** JWT (JSON Web Tokens) & Spring Security
* **Rate Limiting:** Bucket4j
* **Testing:** JUnit 5 & Mockito
* **API Documentation:** Springdoc OpenAPI (Swagger)

---

## ✨ Features Implemented

### Core Requirements
* **User & Role Management:** Supports three distinct roles (`VIEWER`, `ANALYST`, `ADMIN`). A database seeder automatically initializes roles and a default Admin user upon application startup.
* **Financial Records Management:** Complete CRUD operations for income/expense tracking with secure ownership validation.
* **Dashboard Summaries:** Aggregated database-level queries (`@Query`) calculating total income, total expenses, net balance, and category-wise breakdowns.
* **Access Control:** Method-level security (`@PreAuthorize`) enforcing strict RBAC rules across all controller endpoints.
* **Global Exception Handling:** Implemented `@RestControllerAdvice` to intercept validation errors, malformed JSON, and missing resources, ensuring the API always returns clean, predictable HTTP JSON responses.

### Optional Enhancements Included
* **JWT Authentication:** Replaced standard Basic Auth with stateless, token-based security. A custom JWT Filter handles token validation on every request.
* **Pagination & Sorting:** Optimized data retrieval using Spring Data's `Pageable` to prevent database overloading on large datasets.
* **Search Functionality:** Dynamic, case-insensitive searching across transaction categories and notes.
* **Soft Deletes:** Configured Hibernate `@SQLDelete` and `@SQLRestriction` to preserve financial history. Records are marked as deleted rather than permanently erased from the database.
* **Rate Limiting:** Implemented a Bucket4j filter to restrict API usage to 20 requests per minute per IP address, protecting against spam and brute-force attacks.
* **Unit Testing:** The `FinancialRecordService` business logic is fully verified using JUnit 5 and Mockito, ensuring calculations and mapping behave correctly without a database connection.
* **API Documentation:** Interactive Swagger UI automatically generated via OpenAPI.

---

## 🧠 Architecture & Trade-offs

* **Separation of Concerns:** The application strictly adheres to a layered architecture (`Controller -> Service -> Repository`). Database entities are never exposed to the client; Request/Response `DTOs` (utilizing Java Records) are used to enforce input validation and format outputs.
* **Database Choice (H2):** An in-memory H2 database was selected over PostgreSQL/MySQL to ensure the reviewer can run and evaluate the application instantly without needing Docker or local database configuration.
* **Security Flow:** Passwords are never stored in plain text; they are securely hashed using `BCryptPasswordEncoder`.

---

## 🛠️ How to Run the Application

### Prerequisites
* Java 21 installed
* Maven installed (or use the included `mvnw` wrapper)

### Startup Steps
1. Clone this repository and navigate to the root directory.
2. Run the application using the Maven wrapper:
   ```bash
The application will start on port `8080`.

### Default Credentials
Upon startup, the `DatabaseSeeder` component automatically creates an Admin user for testing purposes:
* **Username:** `admin`
* **Password:** `admin123`

---

## 📖 API Documentation & Testing (Swagger)
This API is fully documented and testable using the built-in Swagger UI.

1. Start the application.
2. Navigate your browser to: `http://localhost:8080/swagger-ui.html`
3. Locate the `POST /api/v1/auth/login` endpoint.
4. Input the default credentials to generate a token:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
5. Copy the token string from the response.
6. Scroll to the top of the Swagger page, click the green Authorize button, and paste your token (do not type "Bearer", just the token).
7. You can now securely test all other endpoints (GET, POST, DELETE) directly from your browser!

## 🧪 Running Tests
To execute the unit tests, run the following command in your terminal:

Bash
./mvnw test