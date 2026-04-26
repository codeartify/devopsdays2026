package com.codeartify.membership.contexts.managingmemberships.domain

import org.axonframework.serialization.Revision

@Revision("3.0")
data class MembershipActivatedEvent(
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planTerms: PlanTerms
)
