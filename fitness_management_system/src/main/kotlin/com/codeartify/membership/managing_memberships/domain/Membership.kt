package com.codeartify.membership.managing_memberships.domain

import com.codeartify.membership.managing_memberships.domain.commands.ActivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.PauseMembershipCommand
import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipPausedEvent
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus.ACTIVE
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender

@EventSourced(idType = MembershipId::class)
class Membership {

    private lateinit var membershipId: MembershipId
    private lateinit var customerId: CustomerId
    private var status: MembershipStatus = ACTIVE

    @EntityCreator
    constructor()

    /*
    ## 2 Apply the command on the Membership aggregate
        * append the command values to the event store as a new ActivateMembershipEvent
        * Make sure you also implement the @EventSourcingHandler on the ActivateMembershipEvent so the aggregate can be rebuilt from the event store
     */
    companion object {
        @JvmStatic
        @CommandHandler
        fun activate(cmd: ActivateMembershipCommand, eventAppender: EventAppender): MembershipId {
            eventAppender.append(
                MembershipActivatedEvent(
                    membershipId = cmd.membershipId!!,
                    customerId = cmd.customerId!!
                    // TODO: add any additional fields
                )
            )

            return cmd.membershipId
        }

    }

    @CommandHandler
    fun pause(cmd: PauseMembershipCommand, eventAppender: EventAppender) {
        // TODO: Implement Pause Invariants
        eventAppender.append(MembershipPausedEvent(cmd.membershipId))
    }


    @EventSourcingHandler
    fun on(evt: MembershipActivatedEvent) {
        status = ACTIVE
        membershipId = evt.membershipId
        customerId = evt.customerId
        // TODO: Implement additional activation state changes
    }

    @EventSourcingHandler
    fun on(evt: MembershipPausedEvent) {
        // TODO: Implement Pause state changes
    }


}
