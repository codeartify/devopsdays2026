# Exercises

## 1 Send a command
* send a command to activate a membership (ActivateMembershipUseCase)
  * the command should contain:
    * the customer ID (value object, not null)
    * the agreed upon plan terms containing
      * the plan ID
      * plan duration in months
      * plan price
    * the customer eligibility snapshot
      * a customers date of birth 
      * if the customer was signed by a guardian if they were underage
  * ensure the customer exists (use the local customer cache)
  * ensure the customer does not already have an active membership (use the local membership projection)
  * get the current plan terms for the plan ID
    * the plan repository is owned by another bounded context in the same application: don't directly access it (could be an API call in the future)
    * instead, implement an adapter that uses that repository and create a value object only for the plan terms needed in the membership
  * use Axon's commandGateway.sendAndWait(...) to send the command to the Membership aggregate

## 2 Apply the command on the Membership aggregate

* append the command values to the event store as a new ActivateMembershipEvent
* Make sure you also implement the @EventSourcingHandler on the ActivateMembershipEvent so the aggregate can be rebuild
  from the event store

# 3 React to the ActivateMembershipEvent

* Use @EventHandler on the methods that should react on the event

1. update the local membership projection (used in the ActivateMembershipUseCase to check if the customer already has an
   active membership)
2. Create an invoice in the MembershipInvoicePolicy, store it in the local invoice repository, and raise a "
   InvoiceIssuedEvent".

# 4. Send out an email to the customer for a new InvoiceIssuedEvent

* Use @EventHandler in MembershipInvoiceEmailNotificationPolicy to react on the event
* Get the customer's email from the local customer cache repository
* Use the EmailSender interface to send an email to the customer with the invoice details

# 5. Implement the Membership lifecycle

See the different endpoints in managing_membership.use_case.

## Transition Table

| From State  | Command                | Event                   | To State    | Rule / Invariant                                                              |
|-------------|------------------------|-------------------------|-------------|-------------------------------------------------------------------------------|
| none        | `ActivateMembership`   | `MembershipActivated`   | `ACTIVE`    | Customer is eligible; plan terms are known; membership does not already exist |
| `ACTIVE`    | `PauseMembership`      | `MembershipPaused`      | `PAUSED`    | Only active memberships can be paused; pause period must be valid             |
| `PAUSED`    | `ResumeMembership`     | `MembershipResumed`     | `ACTIVE`    | Only paused memberships can be resumed                                        |
| `ACTIVE`    | `SuspendMembership`    | `MembershipSuspended`   | `SUSPENDED` | Only active memberships can be suspended                                      |
| `SUSPENDED` | `ReactivateMembership` | `MembershipReactivated` | `ACTIVE`    | Only suspended memberships can be reactivated                                 |
| `ACTIVE`    | `CancelMembership`     | `MembershipCancelled`   | `CANCELLED` | Active memberships can be cancelled                                           |
| `PAUSED`    | `CancelMembership`     | `MembershipCancelled`   | `CANCELLED` | Paused memberships can be cancelled                                           |
| `SUSPENDED` | `CancelMembership`     | `MembershipCancelled`   | `CANCELLED` | Suspended memberships can be cancelled                                        |
| `CANCELLED` | any command            | rejected                | `CANCELLED` | Cancelled is terminal - must be checked on every state transition             |

* Make sure to update the MembershipEntity on state changes (see MembershipProjection)

## 6. Reporting Projections
* Growth curve: for each month and year, number of memberships per month activated
* Send a bonus email for all customers that have ever paused their memberships
* Categorise members for marketing purposes into 
  * Motivated (always active, never paused or suspended)
  * Should be motivated (paused)
  * Suspicious (> 1 suspended)
  * Need habit forming (paused > 1)
  * Those who canceled their membership within 3 months of activation
* Find month where customers cancel their membership quickly

