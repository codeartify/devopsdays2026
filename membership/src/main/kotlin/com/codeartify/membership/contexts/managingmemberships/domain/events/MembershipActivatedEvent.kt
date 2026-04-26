package com.codeartify.membership.contexts.managingmemberships.domain.events

import com.codeartify.membership.contexts.managingmemberships.domain.CustomerId
import com.codeartify.membership.contexts.managingmemberships.domain.MembershipId
import com.codeartify.membership.contexts.managingmemberships.domain.values.PlanTerms
import org.axonframework.serialization.Revision

@Revision("3.0")
data class MembershipActivatedEvent(
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planTerms: PlanTerms
)
