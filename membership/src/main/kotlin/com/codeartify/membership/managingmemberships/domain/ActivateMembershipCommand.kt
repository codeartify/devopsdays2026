package com.codeartify.membership.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ActivateMembershipCommand(
    @TargetAggregateIdentifier
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planId: PlanId,
    val signedByGuardian: Boolean = false
)
