package com.codeartify.membership.managing_memberships.domain.events

import com.codeartify.membership.managing_memberships.domain.MembershipId
import org.axonframework.eventsourcing.annotation.EventTag
import org.axonframework.messaging.eventhandling.annotation.Event

@Event(version = "1.0")
data class MembershipSuspendedEvent(
    @EventTag(key = "Membership")
    val membershipId: MembershipId
)
