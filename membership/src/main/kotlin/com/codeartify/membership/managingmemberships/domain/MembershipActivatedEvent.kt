package com.codeartify.membership.managingmemberships.domain

import org.axonframework.serialization.Revision

@Revision("2.0")
data class MembershipActivatedEvent(
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planId: PlanId
)
