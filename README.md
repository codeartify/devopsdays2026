# Event-Sourced DDD with Axon 

This repository contains a small multi-service fitness management system built around Event Sourcing, CQRS, and Kafka-based integration.

## Services

- `identity` on `http://localhost:8082`
  - manages customers
  - persists customer read models in PostgreSQL
  - publishes customer integration events to Kafka

- `membership` on `http://localhost:8081`
  - manages plans and memberships
  - stores Axon events and membership projections in PostgreSQL
  - consumes customer integration events from Kafka
  - issues billing events when memberships are activated
  - notifies customers about invoices

## Infrastructure

- PostgreSQL for `identity` on port `5433`
- PostgreSQL for `membership` on port `5434`
- Kafka on port `9092`
- Axon Server is not used in this setup

## Prerequisites

- Java 25
- Docker and Docker Compose
- Maven 3.9+

## Start The System

### 1. Start infrastructure

```bash
docker compose up
```

This starts:
- `identity-db` on `localhost:5433`
- `fitness-management-db` on `localhost:5434`
- Kafka on `localhost:9092`

### 2. Start the `identity` service

In a new terminal:

```bash
cd identity
./mvnw spring-boot:run
```

### 3. Start the `membership` service

In another terminal:

```bash
cd membership
./mvnw spring-boot:run
```

## Request Files

The repo already contains IntelliJ HTTP client files under [`resources/requests`](./resources/requests):

- [`r_customer.http`](./resources/requests/r_customer.http)
- [`r_plans.http`](./resources/requests/r_plans.http)
- [`r_membership.http`](./resources/requests/r_membership.http)
- [`http-client.env.json`](./resources/requests/http-client.env.json)

These files store `customerId`, `planId`, and `membershipId` for the next requests.

## Suggested Walkthrough

### 1. Create a customer in `identity`

Use [`r_customer.http`](./resources/requests/r_customer.http):

```http
POST http://localhost:8082/customers
Content-Type: application/json

{
  "name": "New Member",
  "dateOfBirth": "1987-08-12",
  "email": "info@codeartify.com"
}
```

### 2. Create a plan in `membership`

Use [`r_plans.http`](./resources/requests/r_plans.http):

```http
POST http://localhost:8081/plans
Content-Type: application/json

{
  "title": "1 Month",
  "description": "Flexible monthly membership plan.",
  "price": 139,
  "durationInMonths": 1
}
```

### 3. Activate a membership

Use [`r_membership.http`](./resources/requests/r_membership.http):

```http
POST http://localhost:8081/memberships/activate
Content-Type: application/json

{
  "customerId": "{{customerId}}",
  "planId": "{{planId}}",
  "signedByGuardian": false
}
```

### 4. Continue the membership lifecycle

The membership service currently exposes:

- `POST /memberships/{membershipId}/pause`
- `POST /memberships/{membershipId}/reactivate`
- `POST /memberships/{membershipId}/suspend`

The plan management API exposes:

- `POST /plans`
- `GET /plans`
- `PUT /plans/{planId}`
- `DELETE /plans/{planId}`

The customer API exposes:

- `POST /customers`
- `GET /customers/{id}`
- `GET /customers`
- `PUT /customers/{id}`
- `DELETE /customers/{id}`

## Data Flow

At a high level:

1. `identity` creates and updates customers.
2. Customer changes are published to Kafka on `managing-customer.integration-events.v1`.
3. `membership` consumes those customer integration events and keeps a local customer cache for membership operations.
4. `membership` manages plans and membership lifecycle state.
5. Membership activation triggers downstream billing behavior inside the membership bounded context.

## Notes

- Both services use PostgreSQL for data storage.
- `membership` uses Axon with PostgreSQL-backed event storage.
- Kafka is used only for cross-service integration, not as the Axon event store.
