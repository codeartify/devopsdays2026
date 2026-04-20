package com.codeartify.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ReactivateMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
