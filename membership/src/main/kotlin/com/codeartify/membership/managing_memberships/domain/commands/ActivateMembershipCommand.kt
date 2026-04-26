package com.codeartify.membership.managing_memberships.domain.commands

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.PlanId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.messaging.commandhandling.annotation.Command
import org.axonframework.modelling.annotation.TargetEntityId

@Command(routingKey = "membershipId")
data class ActivateMembershipCommand(
    @TargetEntityId
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val planId: PlanId,
    val planTerms: PlanTerms,
    val signedByGuardian: Boolean = false
)
