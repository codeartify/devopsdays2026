# Description

identity
└── CustomerAggregate              ← external IAM-like system
publishes CustomerRegistered/CustomerUpdated to Kafka

fitness-management
├── customer-cache
│   └── CustomerProjection         ← local table from Kafka
│
├── managing-plans
│   └── Plan CRUD                  ← simple state-based reference data
│
├── managing-memberships
│   └── MembershipAggregate        ← main Axon aggregate
│
├── billing
│   └── FakeBillingService / ACL   ← creates invoice DTO
│
└── notifying
└── EmailSender                ← sends invoice email

## Managing Plans
* Plans are simple CRUD based entities. There is no need to have event-sourced plans bc. in this system they are simply condition holders. They don't have real lifecycle events that need to be tracked. 


# Reporting
* create a reporting view that shows typical statistics like 
  * a list of plans sotred by number of times they were selected
  * A list of average per month number of new / reneweed memberships
  * The average time between creation and pausing/suspending/cancelling of a membershiop
