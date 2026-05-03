# Event-Sourced DDD with Axon

This repository contains a small multi-service fitness management system built around Event Sourcing, CQRS, and
Kafka-based integration.

## License

This project is the property of Codeartify GmbH and may only be used under the terms of the
[Codeartify Workshop License Agreement](./LICENSE.md).

## Services

- `identity` on `http://localhost:8082`
    - manages customers
    - persists customer read models in PostgreSQL
    - publishes customer registration integration events to Kafka

- `fitness_management_system` on `http://localhost:8081`
    - manages plans and memberships
    - stores Axon events and membership projections in PostgreSQL
    - consumes customer integration events from Kafka
    - issues billing events when memberships are activated
    - notifies customers about invoices

## Infrastructure

- PostgreSQL for `identity` on port `5433`
- PostgreSQL for `fitness_management_system` on port `5434`
- Kafka on port `9092`
- Axon Server is not used in this setup

## Prerequisites

- Java 25
- Docker and Docker Compose
- Maven 3.9+

## Start The System

### One-click start

Double-click [`start-dev.command`](./start-dev.command), or run:

```bash
./start-dev.sh
```

In IntelliJ, use the shared `Start All` run configuration.

This starts Docker Compose first, then starts `identity` and `fitness_management_system`.
Logs are written to `.dev-logs/`.

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

### 3. Start the `fitness_management_system` service

In another terminal:

```bash
cd fitness_management_system
./mvnw spring-boot:run
```

## Request Files

The repo already contains IntelliJ HTTP client files under [`resources/requests`](./resources/requests):

- [`r_customer.http`](./resources/requests/r_customer.http)
- [`r_plans.http`](./resources/requests/r_plans.http)
- [`r_membership.http`](./resources/requests/r_membership.http)
- [`r_customer_cache.http`](./resources/requests/r_customer_cache.http)
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

### 2. Create a plan in `fitness_management_system`

Use [`r_plans.http`](./resources/requests/r_plans.http):

The customer registration event is consumed asynchronously by `fitness_management_system`.
If you run `fitness_management_system` without `identity` or without Kafka history, use
[`r_customer_cache.http`](./resources/requests/r_customer_cache.http) to backfill the customer cache before activating a
membership.

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

### 4. Query the membership projection

Membership read endpoints return the flat projection stored in PostgreSQL:

```http
GET http://localhost:8081/memberships
Accept: application/json
```

```http
GET http://localhost:8081/memberships/{{membershipId}}
Accept: application/json
```

Example response:

```json
{
  "id": "b84333b2-5ed9-4488-a0d7-edee5110bc20",
  "customerId": "customer-1",
  "planId": "b82a8402-0a42-463a-ad46-096804c25e53",
  "planDuration": 6,
  "planPrice": 599,
  "customerDateOfBirth": "1987-08-12",
  "guardianSignaturePresent": false,
  "status": "ACTIVE",
  "pauseStartDate": null,
  "pauseEndDate": null,
  "pauseDurationDays": null
}
```

### 5. Continue the membership lifecycle

Pause an active membership:

```http
POST http://localhost:8081/memberships/{{membershipId}}/pause
Content-Type: application/json

{
  "durationInDays": 30
}
```

Resume a paused membership:

```http
POST http://localhost:8081/memberships/{{membershipId}}/resume
```

Suspend an active membership:

```http
POST http://localhost:8081/memberships/{{membershipId}}/suspend
```

Reactivate a suspended membership:

```http
POST http://localhost:8081/memberships/{{membershipId}}/reactivate
```

Cancel an active, paused, or suspended membership:

```http
DELETE http://localhost:8081/memberships/{{membershipId}}
```

## API Reference

### Customer API (`identity`, port `8082`)

| Method   | Path              | Description                                                |
|----------|-------------------|------------------------------------------------------------|
| `POST`   | `/customers`      | Create a customer and publish a customer integration event |
| `GET`    | `/customers`      | List customers                                             |
| `GET`    | `/customers/{id}` | Get one customer                                           |
| `PUT`    | `/customers/{id}` | Update a customer in the identity database                 |
| `DELETE` | `/customers/{id}` | Delete a customer                                          |

Create/update request body:

```json
{
  "name": "New Member",
  "dateOfBirth": "1987-08-12",
  "email": "info@codeartify.com"
}
```

Only customer creation currently publishes a `CustomerRegistered` integration event. Customer updates and deletes are
local to the `identity` service and are not propagated to `fitness_management_system`.

### Plan API (`fitness_management_system`, port `8081`)

| Method   | Path              | Description                    |
|----------|-------------------|--------------------------------|
| `POST`   | `/plans`          | Create a plan                  |
| `GET`    | `/plans`          | List plans ordered by duration |
| `PUT`    | `/plans/{planId}` | Update a plan                  |
| `DELETE` | `/plans/{planId}` | Delete a plan                  |

Create/update request body:

```json
{
  "title": "6 Months",
  "description": "Half-year membership plan with better value.",
  "price": 599,
  "durationInMonths": 6
}
```

Plan response:

```json
{
  "id": "b82a8402-0a42-463a-ad46-096804c25e53",
  "title": "6 Months",
  "description": "Half-year membership plan with better value.",
  "price": 599,
  "durationInMonths": 6
}
```

### Membership API (`fitness_management_system`, port `8081`)

| Method   | Path                                     | Description                                       |
|----------|------------------------------------------|---------------------------------------------------|
| `POST`   | `/memberships/activate`                  | Activate a membership                             |
| `GET`    | `/memberships`                           | List flat membership projections                  |
| `GET`    | `/memberships/{membershipId}`            | Get one flat membership projection                |
| `POST`   | `/memberships/{membershipId}/pause`      | Pause an active membership                        |
| `POST`   | `/memberships/{membershipId}/resume`     | Resume a paused membership                        |
| `POST`   | `/memberships/{membershipId}/suspend`    | Suspend an active membership                      |
| `POST`   | `/memberships/{membershipId}/reactivate` | Reactivate a suspended membership                 |
| `DELETE` | `/memberships/{membershipId}`            | Cancel an active, paused, or suspended membership |

Activation request body:

```json
{
  "customerId": "{{customerId}}",
  "planId": "{{planId}}",
  "signedByGuardian": false
}
```

Pause request body:

```json
{
  "durationInDays": 30
}
```

### Customer Cache API (`fitness_management_system`, port `8081`)

This endpoint is useful when running `fitness_management_system` without replaying customer events from `identity`.

| Method | Path              | Description                                                             |
|--------|-------------------|-------------------------------------------------------------------------|
| `POST` | `/customer-cache` | Backfill one customer into the fitness management system customer cache |

Request body:

```json
{
  "id": "{{customerId}}",
  "name": "New Member",
  "dateOfBirth": "1987-08-12",
  "email": "info@codeartify.com"
}
```

## Membership Lifecycle

| From State  | Command                | Event                   | To State    | Rule / Invariant                                                                     |
|-------------|------------------------|-------------------------|-------------|--------------------------------------------------------------------------------------|
| none        | `ActivateMembership`   | `MembershipActivated`   | `ACTIVE`    | Customer is eligible; plan terms are known; membership does not already exist        |
| `ACTIVE`    | `PauseMembership`      | `MembershipPaused`      | `PAUSED`    | Only active memberships can be paused; pause duration must be between 30 and 60 days |
| `PAUSED`    | `ResumeMembership`     | `MembershipResumed`     | `ACTIVE`    | Only paused memberships can be resumed                                               |
| `ACTIVE`    | `SuspendMembership`    | `MembershipSuspended`   | `SUSPENDED` | Only active memberships can be suspended                                             |
| `SUSPENDED` | `ReactivateMembership` | `MembershipReactivated` | `ACTIVE`    | Only suspended memberships can be reactivated                                        |
| `ACTIVE`    | `CancelMembership`     | `MembershipCancelled`   | `CANCELLED` | Active memberships can be cancelled                                                  |
| `PAUSED`    | `CancelMembership`     | `MembershipCancelled`   | `CANCELLED` | Paused memberships can be cancelled                                                  |
| `SUSPENDED` | `CancelMembership`     | `MembershipCancelled`   | `CANCELLED` | Suspended memberships can be cancelled                                               |
| `CANCELLED` | any transition command | rejected                | `CANCELLED` | Cancelled is terminal                                                                |

## Data Flow

At a high level:

1. `identity` creates customers.
2. Customer registrations are published to Kafka on `managing-customer.integration-events.v1`.
3. `fitness_management_system` consumes those customer integration events and keeps a local customer cache for
   membership operations.
4. `fitness_management_system` manages plans and membership lifecycle state.
5. Membership activation triggers downstream billing behavior inside the membership bounded context.

## Event Processing

`fitness_management_system` uses explicit Axon event processor definitions:

| Processor                   | Mode     | Purpose                                                                 |
|-----------------------------|----------|-------------------------------------------------------------------------|
| `membership-invoice-policy` | pooled   | Handles billing policy events and issues invoices                       |
| `membership-projection`     | pooled   | Maintains the flat membership read model                                |
| `notifying-customers`       | pooled   | Sends invoice notifications; starts at the latest token if no token row exists |

The `notifying-customers` processor intentionally starts at the current end of the event stream when its token entry is
missing. This keeps deleted notification tokens from replaying historical invoice events and resending old emails.

Kafka customer-cache consumption is separate from Axon event processing and uses the consumer group
`managing-customer-readmodel`.

## Continuous Integration

GitHub Actions runs [`ci.yml`](./.github/workflows/ci.yml) on pushes and pull requests for the `solutions` branch.
The workflow uses Java 25, checks Docker availability, and runs:

```bash
mvn -B -ntp -Ddocker.compose.skip=true clean verify
```

## Notes

- Both services use PostgreSQL for data storage.
- `fitness_management_system` uses Axon with PostgreSQL-backed event storage.
- Kafka is used only for cross-service integration, not as the Axon event store.
