package com.codeartify.membership.managing_memberships.domain.commands

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.PlanId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ActivateMembershipCommand(
    @TargetAggregateIdentifier
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planId: PlanId,
    val planTerms: PlanTerms,
    val signedByGuardian: Boolean = false
)
