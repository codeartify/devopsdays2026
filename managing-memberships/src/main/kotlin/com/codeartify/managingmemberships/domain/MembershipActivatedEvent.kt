package com.codeartify.managingmemberships.domain

import org.axonframework.serialization.Revision

@Revision("1.0")
data class MembershipActivatedEvent(
    val membershipId: MembershipId,
    val customerId: CustomerId
)
