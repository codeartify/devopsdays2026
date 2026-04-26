package com.codeartify.membership.contexts.managingmemberships.domain.events

 import com.codeartify.membership.contexts.managingmemberships.domain.MembershipId
 import org.axonframework.serialization.Revision

@Revision("1.0")
data class MembershipSuspendedEvent(
    val membershipId: MembershipId
)
