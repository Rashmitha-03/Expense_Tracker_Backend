# Expense Tracker API

A Spring Boot REST API for registering users, authenticating with JWT, and managing and summarizing expenses.

## Technology

- Java 17
- Spring Boot 4.0.2
- Spring Web MVC and Spring Data JPA
- MySQL
- Spring Security with JWT
- springdoc-openapi / Swagger UI

## Prerequisites

- JDK 17 or later
- MySQL running locally

## Setup and run

1. Create the database:

   ```sql
   CREATE DATABASE expense_tracker;
   ```

2. Configure the datasource in `src/main/resources/application.properties`. Set `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` to match your local MySQL setup. Do not commit real credentials.

3. Start the application from the project root:

   **Windows PowerShell**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **macOS / Linux**

   ```bash
   ./mvnw spring-boot:run
   ```

The API listens on `http://localhost:8080`. Hibernate is configured to update the database schema on startup.

## API

Registration and login are public. All other API routes require a JWT access token in the `Authorization` header:

```http
Authorization: Bearer <token>
```

### User routes

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/users/register` | Register a user |
| `POST` | `/api/users/login` | Log in and receive an authentication response |

Registration request example:

```json
{
  "name": "Sam Example",
  "email": "sam@example.com",
  "password": "change-me",
  "age": 30
}
```

Login request example:

```json
{
  "email": "sam@example.com",
  "password": "change-me"
}
```

### Expense routes

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/expenses` | Create an expense |
| `GET` | `/api/expenses` | List expenses; supports `page`, `size`, `sortBy`, and `direction` query parameters |
| `GET` | `/api/expenses/{id}` | Get an expense by ID |
| `PUT` | `/api/expenses/{id}` | Update an expense |
| `DELETE` | `/api/expenses/{id}` | Delete an expense |
| `GET` | `/api/expenses/category/{category}` | List expenses in a category |
| `GET` | `/api/expenses/filter/category?category={category}` | Filter expenses by category |
| `GET` | `/api/expenses/filter/dates?start={yyyy-MM-dd}&end={yyyy-MM-dd}` | Filter expenses by date range |
| `GET` | `/api/expenses/today` | List today's expenses |
| `GET` | `/api/expenses/weekly` | List this week's expenses |
| `GET` | `/api/expenses/monthly?month={month}&year={year}` | List expenses for a month (`month` is 1–12) |
| `GET` | `/api/expenses/totalExpensePerUser` | Get the authenticated user's total expense |
| `GET` | `/api/expenses/category-summary` | Get category-wise expense totals |

Expense create/update request example:

```json
{
  "title": "Lunch",
  "amount": 18.5,
  "category": "Food",
  "paymentMethod": "Card",
  "expenseDate": "2026-10-19"
}
```

For example, create an expense with a token returned by login:

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"title":"Lunch","amount":18.5,"category":"Food","paymentMethod":"Card","expenseDate":"2026-10-19"}'
```

## API documentation

With the application running, open Swagger UI at `http://localhost:8080/swagger-ui/index.html`. The OpenAPI document is available at `http://localhost:8080/v3/api-docs`.
