package com.codeartify.membership.managing_memberships.domain.events

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.eventsourcing.annotation.EventTag
import org.axonframework.messaging.eventhandling.annotation.Event

@Event(version = "3.0")
data class MembershipActivatedEvent(
    @EventTag(key = "Membership")
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planTerms: PlanTerms
)
