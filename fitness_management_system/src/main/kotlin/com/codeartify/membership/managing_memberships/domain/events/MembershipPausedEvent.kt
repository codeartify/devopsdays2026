package com.codeartify.membership.managing_memberships.domain.events

import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.PausePeriod
import org.axonframework.eventsourcing.annotation.EventTag
import org.axonframework.messaging.eventhandling.annotation.Event

@Event(version = "1.0")
data class MembershipPausedEvent(
    @EventTag(key = "Membership")
    val membershipId: MembershipId,
    val pausePeriod: PausePeriod
)
