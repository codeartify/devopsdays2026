# Event Sourcing Cheat Sheet

---

# Core Idea

**Event Sourcing** means: Instead of storing only the current state, store the ordered history of domain events.

```text
MembershipActivated
MembershipPaused
MembershipResumed
MembershipCancelled
```

Current state is rebuilt by replaying those events.

---

# When Event Sourcing Fits

Use it when the domain needs:

* auditability
* lifecycle history
* temporal reasoning: “what was true at time X?”
* replay / rebuild of projections
* explicit domain evolution
* strong traceability

Do **not** event-source everything.

---

# Example Workshop Setup

Example workshop setup:

```text
Membership = event-sourced domain model
Plan = state-based domain model
Billing = simple CRUD
Customer = CRUD with event-driven integration (Kafka)
Notification = simple event-caused email
```

---

# DDD Building Blocks

## Aggregate

Transactional consistency boundary.

```text
Command → Aggregate → Domain Event
```

The aggregate:

* enforces invariants
* protects state consistency
* emits events
* is rebuilt from events in ES

---

## Invariant

Rule that must always be true.

Examples:

```text
Cancelled membership cannot be reactivated
Pause only allowed when ACTIVE
Membership can only be activated once
```

---

## Policy

Reaction after a committed fact.

```text
When MembershipActivated → IssueInvoice
When InvoiceIssued → SendInvoiceEmail
```

Policies are eventually consistent. Invariants are strongly consistent. 

---

# CQRS

**CQRS = Command Query Responsibility Segregation**

```text
Commands → change state
Queries  → read from projections
```

With Event Sourcing:

```text
Command side = aggregate + event store
Query side   = projections / read models
```

Reads may be stale briefly.

---

# Axon Mental Model

```kotlin
@CommandHandler
fun handle(command) {
    // check invariants
    apply(EventHappened(...))
}

@EventSourcingHandler
fun on(event) {
    // mutate aggregate state
}
```

Command handlers decide. Event sourcing handlers remember.

---

# Event Store vs Kafka

| Thing       | Purpose                           |
| ----------- | --------------------------------- |
| Event Store | Internal aggregate history        |
| Kafka       | Cross-bounded-context integration |
| Database    | Current state / projections       |
| Outbox      | Reliable publishing intent        |

Kafka is **not** your aggregate event store.

---

# Domain Event vs Integration Event

## Domain Event

Internal business fact. May change often (and quickly)

```text
MembershipActivated
MembershipPaused
```

---

## Integration Event

Public contract between bounded contexts. Stable and versioned.

```text
MembershipActivatedV1
InvoicePaidV1
```

Integration events need:

* explicit versioning
* backward compatibility
* stable ownership
* schema discipline

---

# Projections

A projection is a query-optimized read model built from events.

Examples:

* membership overview
* monthly activated memberships
* billing dashboard
* marketing segmentation
* customers who ever paused

---

# Projection Properties

Projections should be:

* deterministic
* idempotent
* rebuildable

---

# Event Versioning

Internal event sourcing events evolve with:

```text
upcasters
```

Kafka integration events evolve with:

```text
MembershipActivatedV1
MembershipActivatedV2
Schema Registry / compatibility rules
```

Do not change old event meaning.

---

# Architecture Decision

| Option          | Use when                                       |
| --------------- | ---------------------------------------------- |
| CRUD-first      | simple workflows, low invariants               |
| State-based DDD | meaningful invariants, lifecycle logic         |
| Event Sourcing  | audit/history/replay/temporal reasoning matter |

Pick per bounded context, not globally.

---

# Practical Signals for Event Sourcing

Good indicators:

* many lifecycle transitions
* complex business rules
* legal/financial traceability
* debugging past decisions matters
* need for replay
* “what did we know at that time?”

---

# Bad Signals for Event Sourcing

Bad indicators:

* mostly CRUD screens
* simple reference/master data
* low business volatility
* little audit requirement

---

# Common Pitfalls

* Event-sourcing everything
* Treating Kafka as the event store
* Publishing technical row updates instead of business facts
* Large aggregates with too many responsibilities
* Ignoring idempotency
* Breaking public event schemas
* Requiring synchronous consistency across bounded contexts

---

# Key Design Heuristics

## Aggregates

```text
As small as possible,
as large as necessary.
```

---

## Events

Prefer:

```text
MembershipSuspended
```

instead of:

```text
MembershipUpdated { status: SUSPENDED }
```

---

## Boundaries

Inside aggregate:

```text
Strong consistency
```

Between bounded contexts:

```text
Eventual consistency
```

---

# Typical Event-Sourced Flow

```text
REST/API
→ Command
→ Aggregate
→ apply(Event)
→ Event Store append
→ Projection updates
→ Integration event via Outbox
→ Kafka
→ Consumer BC
→ Inbox dedupe
→ Policy / Command
```

---

# Workshop Key Sentence

> We event-source the Membership aggregate because lifecycle, rules, history, and traceability matter. We keep Plan, Billing, and Notification simple because event sourcing should be applied where it pays for its complexity.

---

# Learn More

If you want to learn more, or schedule a free 15-minute call to discuss how Event Sourcing, DDD, or Kafka-based integration could help in your domain:

📧 [info@codeartify.com](mailto:info@codeartify.com)

🌐 [codeartify.com](https://codeartify.com)
