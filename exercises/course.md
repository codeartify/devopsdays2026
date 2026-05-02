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
### Example EventStorming

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
