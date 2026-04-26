package com.codeartify.membership.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class PauseMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
