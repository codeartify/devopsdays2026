package com.codeartify.membership.managing_memberships.domain.events

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.serialization.Revision

@Revision("3.0")
data class MembershipActivatedEvent(
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planTerms: PlanTerms
)
