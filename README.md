# banking-microservices-platform
A scalable and modular banking platform built using microservices architecture. It includes services for customer management, account operations, transaction processing, and notifications, designed to ensure reliability, consistency, and high performance for modern financial applications.

# Account Management API

## Overview
The Account Management API is a RESTful service that allows users to manage bank accounts. The API supports functionalities such as creating accounts, retrieving account details, updating account information, closing accounts, and fetching all accounts.

---

## Features
- **Create Account**: Add a new account.
- **Retrieve Account Details**: Get details of an account by account number.
- **Update Account**: Modify account details.
- **Close Account**: Deactivate an account.
- **List All Accounts**: Fetch a list of all accounts.

---

## Base URL
http://<your-domain>/api/v1/account


Replace `<your-domain>` with your application host (e.g., `localhost:8080`).

---

## API Endpoints

### 1. Create Account
- **Method**: `POST`
- **URL**: `/create`
- **Description**: Creates a new account.
- **Request Body**:
  ```json
  {
    "accountNumber": "string",
    "accountHolderName": "string",
    "balance": "number"
  }

---

### Response
 - ** 201 Created: Returns the created account object.
 - ** 400 Bad Request: If the request data is invalid.

### 2. Get Account Details
- **Method**: `GET`
- **URL**: `/balance`
- **Description**: Retrieves the details of an account using its account number.
- **Query Parameter**:

    accountNumber (String): The account number to search for.

###Response:
- **200 OK: Returns the account object.
- **400 Bad Request: If the account is not found or the request is invalid.
