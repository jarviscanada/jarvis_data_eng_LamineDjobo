# Stock Quote App

## Introduction

The Stock Quote App is a Java-based application that simulates a stock portfolio by retrieving stock data from the Alpha Vantage API and persisting it into a PostgreSQL database. The app allows users to view stock information, buy stocks, and sell stocks, all while managing their stock portfolio. It has been implemented using technologies such as JDBC for database interaction, PostgreSQL (PSQL) for storage, Maven (MVN) for project management, and OkHttp for API communication.

## Implementation

### ER Diagram

The application utilizes a relational database for managing stock quotes and positions. Below is a simple ER diagram representing the relationships:


<img width="419" alt="ER diagram" src="https://github.com/user-attachments/assets/b4463c46-db65-4a5b-855b-360feae33b9f">

### Design Patterns

This project makes use of the **DAO (Data Access Object)** design pattern. 

The **DAO pattern** is used to separate the database logic from the business logic. It provides an abstraction over the database layer by defining data access operations in a specific interface. In this app, `QuoteDao` and `PositionDao` are responsible for interacting with the `quote` and `position` tables, respectively, within the PostgreSQL database. By using this design pattern, the database operations are kept cleanly separated from the business logic implemented in the service classes (`QuoteService` and `PositionService`).

The **Repository design pattern** is also applied within this app, which ensures that business logic does not need to know the exact way data is fetched or saved. The service layer interacts with DAOs (repositories) without worrying about SQL queries, ensuring modularity and allowing for easier testing and maintenance of the codebase.

By using these design patterns, the app is more maintainable, testable, and flexible.

## Test

The Stock Quote App is tested using both **unit tests** and **integration tests**. We use **JUnit** to ensure that all methods work as expected in isolation (unit testing), and we leverage **Mockito** for mocking dependencies when necessary. This allows us to verify the behavior of individual components like the `QuoteDao` and `PositionDao` without requiring an active database connection for every test.

For **integration testing**, we verify that the entire system works as a whole, including interaction with the PostgreSQL database. We use real database connections in these tests, ensuring that stock buying, selling, and viewing operations are executed correctly and persisted to the database.

### Unit Testing

- We used JUnit to test each method within the DAO classes (`QuoteDao` and `PositionDao`) without mocking the database connection. This ensures that every part of the application, from fetching stock information to saving positions, works as expected.
- For the service layer (`QuoteService`, `PositionService`), we used **Mockito** to mock external dependencies like the API calls, ensuring that the business logic functions properly, even when external services are unavailable.

### Integration Testing

- Integration tests were conducted by running the entire app in a real environment with a PostgreSQL database. This allowed us to test the app’s functionality end-to-end, ensuring that all parts of the app, including the data persistence, stock retrieval, and business logic, interact correctly.
- **Console testing** was also used by running the application via the `Main` class. We performed operations such as buying, selling, and viewing stocks using the actual console interface. This verified that the user experience and interactions flow as intended.

### Database Setup

- PostgreSQL was used for the underlying database. The database is configured with a `quote` table for storing stock information and a `position` table for tracking user positions.
- Test data for both quotes and positions is inserted during the tests to simulate real trading scenarios, allowing us to verify that data is saved, retrieved, and deleted correctly.

## Deployment

The application is packaged using Maven, and a Docker container is used to set up the PostgreSQL database for testing and production use.

### Steps to Deploy

1. **Register a Docker Hub account** if you don’t already have one.
2. **Package your Java app** using Maven:
   ```bash
   mvn clean package
	```
Build a Docker image for your app:

```bash
docker build -t your-dockerhub-username/stock-quote-app
```
Verify your image:
```bash
docker images
```
Run your Docker container:
```bash
docker run -p 8080:8080 your-dockerhub-username/stock-quote-app
```
Push your image to Docker Hub:
```bash
docker push your-dockerhub-username/stock-quote-app
```
Verify your image on Docker Hub

Quick Start

Option 1: Run the app locally

Clone the repository:
```bash
git clone git@github.com:jarviscanada/jarvis_data_eng_LamineDjobo.git
```
Navigate to the core_java/jdbc directory:
```bash
cd jarvis_data_eng_LamineDjobo/core_java/jdbc
```
Ensure you have Docker installed and running. Start a PostgreSQL container with the following command:
```bash
docker run --name stock_quote -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres
```
Use Maven to build the project:
```bash
mvn clean package
```
Run the application:
```bash
java -jar target/stockquote-app.jar
```

Option 2: Run using Docker

Pull the Docker image from Docker Hub:
```bash
docker pull lamine005/stock-quote-app
```

Run the container:
```bash
docker run -p 8080:8080 lamine005/stock-quote-app
```

You can now interact with the app through the console interface by buying, selling, and viewing stocks.
