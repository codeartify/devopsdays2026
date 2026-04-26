package com.codeartify.membership.contexts.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class PauseMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
