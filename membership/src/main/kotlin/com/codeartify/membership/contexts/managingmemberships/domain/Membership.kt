package com.codeartify.membership.contexts.managingmemberships.domain

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Membership() {

    @AggregateIdentifier
    lateinit var membershipId: MembershipId
    lateinit var customerId: CustomerId
    lateinit var planId: PlanId
    lateinit var status: MembershipStatus

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    fun activate(cmd: ActivateMembershipCommand) {
        apply(MembershipActivatedEvent(cmd.membershipId, cmd.customerId, cmd.planId))
    }

    @CommandHandler
    fun pause(cmd: PauseMembershipCommand) {
        apply(MembershipPausedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun reactivate(cmd: ReactivateMembershipCommand) {
        apply(MembershipReactivatedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun suspend(cmd: SuspendMembershipCommand) {
        apply(MembershipSuspendedEvent(cmd.membershipId))
    }

    @EventSourcingHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipId = evt.membershipId
        customerId = evt.customerId
        planId = evt.planId
        status = MembershipStatus.ACTIVE
    }

    @EventSourcingHandler
    fun on(evt: MembershipPausedEvent) {
        status = MembershipStatus.PAUSED
    }

    @EventSourcingHandler
    fun on(evt: MembershipReactivatedEvent) {
        status = MembershipStatus.ACTIVE
    }

    @EventSourcingHandler
    fun on(evt: MembershipSuspendedEvent) {
        status = MembershipStatus.SUSPENDED
    }
}
