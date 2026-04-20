package com.codeartify.managingmemberships.event

import com.codeartify.managingmemberships.command.CustomerId
import com.codeartify.managingmemberships.command.MembershipId
import org.axonframework.serialization.Revision

@Revision("1.0")
data class MembershipActivatedEvent(
    val membershipId: MembershipId,
    val customerId: CustomerId
)
