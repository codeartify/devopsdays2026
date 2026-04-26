package com.codeartify.membership.managing_memberships.domain.commands

import com.codeartify.membership.managing_memberships.domain.MembershipId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ReactivateMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
