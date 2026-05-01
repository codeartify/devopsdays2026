package com.codeartify.membership.managing_memberships.domain

import com.codeartify.membership.managing_memberships.domain.commands.*
import com.codeartify.membership.managing_memberships.domain.events.*
import com.codeartify.membership.managing_memberships.domain.values.CustomerEligibility
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus
import com.codeartify.membership.managing_memberships.domain.values.PausePeriod
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender

@EventSourced(idType = MembershipId::class)
class Membership {

    private lateinit var membershipId: MembershipId
    private lateinit var customerId: CustomerId
    private lateinit var planTerms: PlanTerms
    private lateinit var status: MembershipStatus
    private lateinit var customerEligibility: CustomerEligibility
    private var pausePeriod: PausePeriod? = null


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
                    planTerms = cmd.planTerms,
                    customerEligibility = cmd.customerEligibility
                )
            )

            return cmd.membershipId
        }

    }

    @CommandHandler
    fun pause(cmd: PauseMembershipCommand, eventAppender: EventAppender) {
        ensureNotCancelled()
        require (status != MembershipStatus.PAUSED) {
            "Membership is already paused"
        }
        require (status == MembershipStatus.ACTIVE) {
            "Cannot pause a non-active membership"
        }
        eventAppender.append(MembershipPausedEvent(cmd.membershipId, cmd.pausePeriod))
    }

    @CommandHandler
    fun suspend(cmd: SuspendMembershipCommand, eventAppender: EventAppender) {
        ensureNotCancelled()
        require(status == MembershipStatus.ACTIVE) {
            "Only active memberships can be suspended"
        }
        eventAppender.append(MembershipSuspendedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun resume(cmd: ResumeMembershipCommand, eventAppender: EventAppender) {
        ensureNotCancelled()
        require(status == MembershipStatus.PAUSED) {
            "Only paused memberships can be resumed"
        }

        eventAppender.append(MembershipResumedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun reactivate(cmd: ReactivateMembershipCommand, eventAppender: EventAppender) {
        ensureNotCancelled()
        require(status == MembershipStatus.SUSPENDED) {
            "Only suspended memberships can be reactivated"
        }

        eventAppender.append(MembershipReactivatedEvent(cmd.membershipId))
    }

    @CommandHandler
    fun cancel(cmd: CancelMembershipCommand, eventAppender: EventAppender) {
        ensureNotCancelled()
        require(status in cancelableStatus()) {
            "Only active, paused, or suspended memberships can be cancelled"
        }

        eventAppender.append(MembershipCancelledEvent(cmd.membershipId))
    }

    private fun cancelableStatus(): Set<MembershipStatus> =
        setOf(MembershipStatus.ACTIVE, MembershipStatus.PAUSED, MembershipStatus.SUSPENDED)

    @EventSourcingHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipId = evt.membershipId
        customerId = evt.customerId
        planTerms = evt.planTerms
        customerEligibility = evt.customerEligibility
        status = MembershipStatus.ACTIVE
    }

    @EventSourcingHandler
    fun on(evt: MembershipPausedEvent) {
        status = MembershipStatus.PAUSED
        pausePeriod = evt.pausePeriod
    }


    @EventSourcingHandler
    fun on(evt: MembershipSuspendedEvent) {
        status = MembershipStatus.SUSPENDED
    }


    @EventSourcingHandler
    fun on(event: MembershipResumedEvent) {
        status = MembershipStatus.ACTIVE
        pausePeriod = null
    }

    @EventSourcingHandler
    fun on(event: MembershipReactivatedEvent) {
        status = MembershipStatus.ACTIVE
    }

    @EventSourcingHandler
    fun on(event: MembershipCancelledEvent) {
        status = MembershipStatus.CANCELLED
        pausePeriod = null
    }

    private fun ensureNotCancelled() {
        require(status != MembershipStatus.CANCELLED) {
            "Cancelled memberships are terminal"
        }
    }

}
