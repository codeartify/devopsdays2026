package com.codeartify.managingmemberships.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class PauseMembershipCommand(
    @TargetAggregateIdentifier val membershipId: String
)
