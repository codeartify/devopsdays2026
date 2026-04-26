package com.codeartify.membership.contexts.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ActivateMembershipCommand(
    @TargetAggregateIdentifier
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planId: PlanId,
    val planTerms: PlanTerms,
    val signedByGuardian: Boolean = false
)
