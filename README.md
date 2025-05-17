# Pharma Supply Chain and Prescription Fulfillment System

A robust Spring Boot application for managing pharmaceutical supply chains and prescription
fulfillment processes. This system handles drug inventory, pharmacy management, and prescription
processing with comprehensive audit logging.

## Project Overview

This system is built using:

- Java 17
- Spring Boot 3.4.5
- PostgreSQL Database
- Flyway for database migrations
- Maven for dependency management
- JPA/Hibernate for ORM
- Checkstyle for code quality

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- PostgreSQL 12 or higher
- IDE (IntelliJ IDEA recommended)

## Setup Instructions

1. Clone the repository:

```bash
git clone <repository-url>
cd pharmaTest
```

2. Configure PostgreSQL:
    - Create a database named `my_db`
    - Update `application.properties` with your database credentials

3. Build the project:

```bash
mvn clean install
```

4. Run database migrations:

```bash
mvn flyway:migrate
```

5. Start the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Drugs API

- `GET /api/v1/drugs/{id}` - Get drug details by ID
- `POST /api/v1/drugs` - Add a new drug to inventory

### Pharmacies API

- `GET /api/v1/pharmacies` - Get all pharmacies and the contracted drugs
- `POST /api/v1/pharmacies/{pharmacyId}/prescriptions` - Create a new prescription
- `POST /api/v1/pharmacies/prescriptions/{prescriptionId}/fulfill` - Fulfill a prescription

### Audit Logs API
- `GET /api/v1/audit-logs` - Get paginated audit logs with optional filters
    - Query Parameters:
        - `patientId` (optional): Filter logs by patient ID
        - `pharmacyId` (optional): Filter logs by pharmacy ID
        - `status` (optional): Filter logs by status (SUCCESS or FAILURE)
        - `page` (optional, default: 0): Page number for pagination
        - `size` (optional, default: 10): Number of items per page
    - Example Response:
      ```json
      {
          "content": [
              {
                  "id": 2,
                  "prescriptionId": 1,
                  "patientId": 1,
                  "pharmacyId": 1,
                  "drugsRequested": [
                      {
                          "drugId": 1,
                          "quantity": 11,
                          "dosage": "500mg"
                      }
                  ],
                  "drugsDispensed": [
                      {
                          "drugId": 1,
                          "name": "Aspirin",
                          "manufacturer": "Bayer",
                          "batchNumber": "ASP123",
                          "quantity": 11
                      }
                  ],
                  "failureReason": null,
                  "status": "SUCCESS",
                  "createdAt": "2025-05-17T22:17:33.04469"
              },
              {
                  "id": 5,
                  "prescriptionId": 1,
                  "patientId": 1,
                  "pharmacyId": 1,
                  "drugsRequested": [],
                  "drugsDispensed": [
                      {
                          "drugId": 1,
                          "name": "Aspirin",
                          "manufacturer": "Bayer",
                          "batchNumber": "ASP123",
                          "quantity": 11
                      }
                  ],
                  "failureReason": null,
                  "status": "SUCCESS",
                  "createdAt": "2025-05-17T22:19:54.027963"
              }
          ],
          "pageable": {
              "pageNumber": 0,
              "pageSize": 10,
              "sort": {
                  "empty": true,
                  "sorted": false,
                  "unsorted": true
              },
              "offset": 0,
              "paged": true,
              "unpaged": false
          },
          "last": true,
          "totalPages": 1,
          "totalElements": 2,
          "first": true,
          "size": 10,
          "number": 0,
          "sort": {
              "empty": true,
              "sorted": false,
              "unsorted": true
          },
          "numberOfElements": 2,
          "empty": false
      }
      ```

## Testing Instructions

1. Run unit tests:

```bash
mvn test
```

## Linting Instructions

The project uses Checkstyle with Google Java Style Guide. To run the linter:

```bash
mvn checkstyle:check
```

## Assumptions

1. **Drug Management**:
    - Drugs have unique batch numbers
    - Expired drugs cannot be added to inventory
    - Stock quantities cannot be negative

2. **Pharmacy Operations**:
    - Pharmacies are contracted for specific drugs
    - Each pharmacy has allocation limits for contracted drugs
    - Prescriptions must be fulfilled within allocation limits

3. **Prescription Processing**:
    - All drugs in a prescription must be available
    - Prescriptions are atomic (all-or-nothing fulfillment)
    - Failed prescriptions are logged with reasons

4. **Audit Logging**:
    - All prescription attempts are logged
    - Logs include success/failure status and reasons
    - Audit logs are immutable

## Error Handling

The system implements comprehensive error handling:

- Resource not found (404)
- Invalid input (400)
- Invalid state (400)
- Internal server errors (500)

All errors return a standardized response format:

```json
{
  "error": "Error type",
  "message": "Detailed error message"
}
``` 