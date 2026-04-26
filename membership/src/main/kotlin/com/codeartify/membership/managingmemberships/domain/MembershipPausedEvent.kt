package com.codeartify.membership.managingmemberships.domain

 import org.axonframework.serialization.Revision

@Revision("1.0")
data class MembershipPausedEvent(
    val membershipId: MembershipId
)
