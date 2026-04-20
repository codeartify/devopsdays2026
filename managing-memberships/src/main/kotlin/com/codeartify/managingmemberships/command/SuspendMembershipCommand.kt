package com.codeartify.managingmemberships.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class SuspendMembershipCommand(
    @TargetAggregateIdentifier val membershipId: String
)
