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
    - Each time a drug is added to the inventory, a new batch is created even if the drug name
      matches an existing one (as each addition corresponds to a new batch with a unique batch
      number and expiry date).
    - Drugs have unique batch numbers
    - Expired drugs cannot be added to inventory
    - Stock quantities cannot be negative

2. **Pharmacy Operations**:
    - Pharmacies contract for specific drug batches (meaning even with the same drug name in
      inventory, pharmacies must contract separately for each batch).
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