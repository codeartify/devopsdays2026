package com.codeartify.managingmemberships.command

import com.codeartify.managingmemberships.event.MembershipActivatedEvent
import com.codeartify.managingmemberships.event.MembershipPausedEvent
import com.codeartify.managingmemberships.event.MembershipReactivatedEvent
import com.codeartify.managingmemberships.event.MembershipSuspendedEvent
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
    lateinit var status: MembershipStatus

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    fun activate(cmd: ActivateMembershipCommand) {
        apply(MembershipActivatedEvent(cmd.membershipId))
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
