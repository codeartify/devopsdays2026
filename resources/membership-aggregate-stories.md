# Membership Aggregate Stories (Axon / DDD)

## Overview
This document contains implementable Membership aggregate stories derived from business rules.

---

## 1. Activate Membership

**Command**
ActivateMembership(customerId, planId, signedByGuardian?)

**Invariants**
- Customer must not already have an ACTIVE membership
- If customer < 18 → guardian signature required
- Membership becomes ACTIVE immediately

**Events**
- MembershipActivated

---

## 2. Payment Dependency

**Invariant**
If invoice not fully paid:
- cannot Pause
- cannot Upgrade

**Events**
- MembershipMarkedAsPaid
- MembershipMarkedAsUnpaid

---

## 3. Pause Membership

**Command**
PauseMembership(startDate, endDate, reason)

**Invariants**
- Must be ACTIVE
- Invoice must be paid
- Duration: 3 weeks – 2 months
- Proof required

**Events**
- MembershipPaused

---

## 4. Cancel Membership

**Command**
CancelMembership(requestDate)

**Invariants**
- Respect notice period

**Events**
- MembershipCancelled

---

## 5. Renew Membership

**Command**
RenewMembership

**Invariants**
- Payment must be before expiry

**Events**
- MembershipRenewed

---

## 6. Upgrade Membership

**Command**
UpgradeMembership(newPlan)

**Invariants**
- ACTIVE
- Not blocked
- Paid

**Events**
- MembershipUpgraded

---

## 7. Block Membership (Non-payment)

**Command**
BlockMembership

**Invariants**
- Invoice overdue

**Events**
- MembershipBlocked

---

## 8. Reactivate Membership

**Command**
ReactivateMembership

**Invariants**
- Was BLOCKED or CANCELLED
- Full payment required

**Events**
- MembershipReactivated

---

## 9. Expire Membership

**Command**
ExpireMembership

**Events**
- MembershipExpired

---

## Notes

- Billing is handled in another bounded context
- Membership only reacts to payment state
- Time-based logic is handled via policies
