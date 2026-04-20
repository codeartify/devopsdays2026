package com.codeartify.managingmemberships.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ActivateMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
