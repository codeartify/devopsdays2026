package com.codeartify.managingmemberships.event

import org.axonframework.serialization.Revision

@Revision("1.0")
data class MembershipPausedEvent(
    val membershipId: String
)
