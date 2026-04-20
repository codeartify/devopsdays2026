package com.codeartify.managingmemberships.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ReactivateMembershipCommand(
    @TargetAggregateIdentifier val membershipId: String
)
