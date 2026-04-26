package com.codeartify.membership.contexts.managingmemberships.domain.commands

import com.codeartify.membership.contexts.managingmemberships.domain.CustomerId
import com.codeartify.membership.contexts.managingmemberships.domain.MembershipId
import com.codeartify.membership.contexts.managingmemberships.domain.values.PlanId
import com.codeartify.membership.contexts.managingmemberships.domain.values.PlanTerms
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ActivateMembershipCommand(
    @TargetAggregateIdentifier
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planId: PlanId,
    val planTerms: PlanTerms,
    val signedByGuardian: Boolean = false
)
