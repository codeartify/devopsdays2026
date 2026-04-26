package com.codeartify.membership.managing_memberships.domain.commands

import com.codeartify.membership.managing_memberships.domain.MembershipId
import org.axonframework.messaging.commandhandling.annotation.Command
import org.axonframework.modelling.annotation.TargetEntityId

@Command(routingKey = "membershipId")
data class SuspendMembershipCommand(
    @TargetEntityId val membershipId: MembershipId
)
