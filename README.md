# Xpress Payments Java Backend Exercise

This project is a backend service for a payment company, designed to authenticate users using JWT and provide airtime payments through a Virtual Top-Up (VTU) API. It includes user authentication, secure payment processing, and integration with external services. The codebase features unit tests for validation with at least 50% code coverage.

## Prerequisites

- **Java 17**: Ensure you have JDK 17 installed on your machine.
- **PostgreSQL**: Install and set up PostgreSQL for the database.
- **Maven**: Use Maven for building and managing project dependencies.

## Getting Started

### Installation

1. Clone the repository and navigate into the project directory.
2. Set up your PostgreSQL database by creating a new database named `billpayment_assessment` and updating the database connection settings in the `application.yml` file.
3. Build the project using Maven.
4. Run the application with Spring Boot.

### Running Tests

Unit tests are included in the project to validate the code coverage.

### Notes

Ensure you have a valid account on the Biller Service Test environment portal for VTU API access. Use JWT for secure API authentication, as defined in the `application.yml` file.
