package com.codeartify.membership.managing_memberships.domain.events

 import com.codeartify.membership.managing_memberships.domain.MembershipId
 import org.axonframework.serialization.Revision

@Revision("1.0")
data class MembershipPausedEvent(
    val membershipId: MembershipId
)
