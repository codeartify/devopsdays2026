## Introduction to Event Sourcing in DDD

* persistence model based on domain events
* useful for aggregates with meaningful lifecycle history
* not a default choice for every model

---

### Core Idea

* store what happened
* derive what is true now
* event stream = aggregate history
* current state = result of replaying events

---

### What is a Domain Event

* meaningful business fact
* happened in the domain
* named in past tense
* expressed using ubiquitous language
* relevant to domain experts
* sequence forms a domain story

---

### Aggregate Perspective

* aggregate decides whether a command is valid
* aggregate records domain events as outcomes
* events capture state transitions
* invariants protected before events are recorded

Example:

```text
Command: PauseMembership
Rule: only active memberships can be paused
Event: MembershipPaused
```

---

### Example (Membership)

```
MembershipActivated
MembershipPlanChanged
MembershipPaused
MembershipReactivated
```

* each event = business fact
* sequence = lifecycle story
* state rebuilt from ordered events

---

### What This Gives Us

* explicit business history
* audit trail in domain language
* temporal reasoning (state at time X)
* replayability (rebuild projections, debug)
* aligns with Event Storming outputs

---

### Important Distinction

* Domain Event → internal business fact
* Event Store → aggregate event streams
* Projection / Read Model → query-optimized view
* Integration Event → message for other bounded contexts
* Kafka / Event Bus → transport, not the event store

---

### Trade-offs

* higher complexity (event evolution, replay, projections)
* stronger modeling discipline required
* not ideal for simple CRUD models

---

### When It Fits Well

* rich aggregate lifecycle
* strong audit requirements
* temporal reasoning needed
* domain cares about sequence of events

---

### One Sentence Summary

> store aggregate history as domain events
> derive current state from that history

---

## Aggregate in DDD

* consistency boundary
* protects business rules
* controls state changes
* exposes behavior, not setters
* emits domain events
* persisted as one unit

---

### Aggregate Root

* single entry point
* validates commands
* enforces invariants
* coordinates internal objects
* records domain events

Example:

```text
Command: PauseMembership
Aggregate: Membership
Rule: only active memberships can be paused
Event: MembershipPaused
```

---

### Different kinds of business rules

* Validation

    * checks input quality (format, required fields, simple constraints)
    * typically at API / application boundary
    * protects against invalid data entering the system
    * does not protect business state

* Invariants

    * must always be true
    * enforced inside aggregate
    * checked before state changes
    * violation → command rejected
    * protect business state consistency

  Examples:

    * cannot pause twice
    * plan change only when ACTIVE
    * cancelled is terminal

* Policies

    * what should happen next
    * react to domain events
    * can cross aggregates / systems
    * often async / eventual
    * coordinate workflows

  Examples:

    * when MembershipPaused → stop billing
    * when MembershipActivated → issue invoice
    * when InvoicePaid → reactivate membership

---

### One Sentence Summary

> Validation protects inputs
> Aggregates protect what must always be true
> Policies decide what should happen next
