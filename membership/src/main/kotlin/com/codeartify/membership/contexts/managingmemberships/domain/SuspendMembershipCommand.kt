package com.codeartify.membership.contexts.managingmemberships.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class SuspendMembershipCommand(
    @TargetAggregateIdentifier val membershipId: MembershipId
)
