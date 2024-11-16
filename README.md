# Banking Microservices Platform
A scalable and modular banking platform built using microservices architecture. It includes services for customer management, account operations, transaction processing, and notifications, designed to ensure reliability, consistency, and high performance for modern financial applications.

# High-Level Architecture
The System has below services:
- **Customer Service**: Handles customer registration, authentication, and profile updates.
- **Account Service**: Manages bank account operations, including account creation, balance inquiry, and closure, as well as debit and credit operations.
- **Transaction Service**: Manages deposits, withdrawals, and fund transfers, coordinating transactions between accounts.
- **Notification Service**: Sends notifications to customers based on transactional and account events.
- **Message Queue**: Supports asynchronous communication between services, especially for transaction and notification events.

![Untitled Diagram(2)](https://github.com/user-attachments/assets/62be59ca-e802-4c1f-b2a1-0558e6eb44e4)


# Sequence Diagram

![Sequence Diagram(1)](https://github.com/user-attachments/assets/195afca2-cf93-4091-9d59-bac5ea05c2ef)


# Customer Management API

## Overview
The Customer Management Service is a RESTful service that allows users to manage their profile accounts. The API supports functionalities such as registration, Authentication, Update Profile.


## Base URL
http://<your-domain>/api/v1/customer

Replace `<your-domain>` with your application host (e.g., `localhost:8080`).




## Features
- **Registration**: Add a new account.
  - **Request Body**:
    ```json
    {
    "firstName": string,
    "lastName": string,
    "email": string,
    "password": string
    }
  
  - ** Response**:
    - ** 201 Created: Returns the created account object.
    - ** 400 Bad Request: If the request data is invalid.
   


- **Authentication**: Get details of an account by account number.


- **Update Profile**: Modify account details.
  - **Request Body**:
    ```json
    {
    "firstName": string,
    "lastName": string,
    "email": string,
    "password": string
    }

  - ** Response**:
    - ** 201 Created: Returns the created account object.
    - ** 400 Bad Request: If the request data is invalid.
   
---

# Account Management API

## Overview
The Account Management Service is a RESTful service that allows users to manage bank accounts. The API supports functionalities such as creating accounts, retrieving account details, updating account information, closing accounts, and fetching all accounts.

## Base URL
http://<your-domain>/api/v1/account


Replace `<your-domain>` with your application host (e.g., `localhost:8080`).


## Features
- **Create Account**: Add a new account.
  - **Request Body**:
    ```json
    {
    "accountType": "SAVINGS",
    "currencyCode": "KES"
    }
  
  - ** Response**:
    - **201 Created: Returns the created account object.
    - **400 Bad Request: If the request data is invalid.

- **Retrieve Account Details**: Get details of an account by account number.
  - **Method**: `GET`
  - **URL**: `/balance?accountNumber=<account number>`



- **Update Account**: Modify account details.
  - **Method**: `POST`
  - **URL**: `/create`
  - **Request Body**:
    ```json
    {
    "accountNumber": "string",
    "accountHolderName": "string",
    "balance": "number"
    }

- **Close Account**: Deactivate an account.
  - **Method**: `PUT`
  - **URL**: `/close?accountNumber=<account number>`

- **List All Accounts**: Fetch a list of all accounts.
  - **Method**: `GET`
  - **URL**: `/balance/?accountNumber=<account number>`

---

# Transactions Management API

## Overview
The Transactions Management Service is a RESTful service that allows users to transact with the bank. It also acts as the Orchestrator service. The API supports functionalities such as deposits, withdrawals, and fund transfers and coordinating transactions between accounts.

## Base URL
http://<your-domain>/api/v1/transactions


Replace `<your-domain>` with your application host (e.g., `localhost:8080`).



## Features
- **Account Deposits**: Add money to account.
  - **Method**: `POST`
  - **URL**: `/deposit`
  - **Request Body**:
    ```json
     {
    "transactionId": 0,
    "accountId": "string",
    "amount": 0,
    "description": "string",
    "transactionType": "string",
    "debitAccount": "string",
    "creditAccount": "string",
    "transactionReference": "string",
    "transactionCurrency": "string",
    "transactionDate": "2024-11-16T10:17:29.477Z",
    "statusCode": "string",
    "statusMessage": "string"
    }

  - ** Response**:
    - **201 Created: Returns the created account object.
    - **400 Bad Request: If the request data is invalid.


- **Account Withdrawals**: Remove money from account.
  - **Method**: `POST`
  - **URL**: `/withdraw`
  - **Request Body**:
    ```json
    {
    "accountNumber": "string",
    "amount": 0
    }
  
  - ** Response**:
    - **201 Created: Returns the created account object.
    - **400 Bad Request: If the request data is invalid.



- **Fund Transfers**: Move money from one account to another.
  - **Method**: `POST`
  - **URL**: `/fund-transfer`
  - **Request Body**:
    ```json
    {
    "transactionId": 0,
    "accountId": "string",
    "amount": 0,
    "description": "string",
    "transactionType": "string",
    "debitAccount": "string",
    "creditAccount": "string",
    "transactionReference": "string",
    "transactionCurrency": "string",
    "transactionDate": "2024-11-16T10:18:36.711Z",
    "statusCode": "string",
    "statusMessage": "string"
    }
  
  - ** Response**:
    - **201 Created: Returns the created account object.
    - **400 Bad Request: If the request data is invalid.

- - **Generate Notification**: Generate Notifications and push to ActiveMQ.

---

# Notification Management API

## Overview
The Notification Management Service is a RESTful service that consumes neceives notification message (Email's and SMS) and sends them to a 3rd party service.

---
# Environment Requirements

## Ensure the following tools are installed on your machine:

Java:
    java --version  
    java 21.0.5 2024-10-15 LTS  

Maven:
    mvn --version  
    Apache Maven 3.9.9  

Docker Compose:
    Docker version 27.3.1, build ce12230 
    
---

# Running the Services

Follow these steps to set up and run the microservices:

    Clone the repository:

git clone <repository_url>  

Navigate to a specific microservice:

cd <service_directory>  

Example:

cd accounts/banking-microservices-platform  

Generate the JAR file using Maven:

mvn clean package  

Build Docker containers:

docker compose up --build -d  

Run the service:

    docker run -d -p <host_port>:<container_port> <image_name>  

    Replace:
        <host_port>: Desired port on your machine
        <container_port>: Container port exposed by the service
        <image_name>: Docker image name generated during the build process
        
---

# Swagger Documentation

The platform provides Swagger UI endpoints for API exploration and testing. Access them as follows:

    Customer Service:
    http://localhost:8080/swagger-ui/index.html

    Account Service:
    http://localhost:8081/swagger-ui/index.html#/account-controller/updateAccount

    Transaction Service:
    http://localhost:8082/swagger-ui/index.html#/transactions-controller/fundsTransfer

