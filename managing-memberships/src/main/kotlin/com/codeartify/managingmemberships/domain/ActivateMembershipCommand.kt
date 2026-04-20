package com.codeartify.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ActivateMembershipCommand(
    @TargetAggregateIdentifier
    val membershipId: MembershipId,
    val customerId: CustomerId
)
