package com.codeartify.membership.contexts.managingmemberships.domain.commands

import com.codeartify.membership.contexts.managingmemberships.domain.MembershipId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class PauseMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
