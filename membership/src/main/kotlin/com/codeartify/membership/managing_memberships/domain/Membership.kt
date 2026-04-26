package com.codeartify.membership.managing_memberships.domain

import com.codeartify.membership.managing_memberships.domain.commands.ActivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.PauseMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.ReactivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.SuspendMembershipCommand
import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipPausedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipReactivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipSuspendedEvent
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender

@EventSourced(idType = MembershipId::class)
class Membership {

    lateinit var membershipId: MembershipId
    lateinit var customerId: CustomerId
    lateinit var planTerms: PlanTerms
    lateinit var status: MembershipStatus

    @EntityCreator
    constructor()

    companion object {
        @JvmStatic
        @CommandHandler
        fun activate(cmd: ActivateMembershipCommand, eventAppender: EventAppender): MembershipId {
            eventAppender.append(
                MembershipActivatedEvent(
                    membershipId = cmd.membershipId,
                    customerId = cmd.customerId,
                    planTerms = cmd.planTerms
                )
            )

            return cmd.membershipId
        }
    }

    @CommandHandler
    fun pause(cmd: PauseMembershipCommand, eventAppender: EventAppender) {
        eventAppender.append(MembershipPausedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun reactivate(cmd: ReactivateMembershipCommand, eventAppender: EventAppender) {
        eventAppender.append(MembershipReactivatedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun suspend(cmd: SuspendMembershipCommand, eventAppender: EventAppender) {
        eventAppender.append(MembershipSuspendedEvent(cmd.membershipId))
    }

    @EventSourcingHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipId = evt.membershipId
        customerId = evt.customerId
        planTerms = evt.planTerms
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
