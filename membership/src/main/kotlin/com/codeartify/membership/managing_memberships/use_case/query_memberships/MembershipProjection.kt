package com.codeartify.membership.managing_memberships.use_case.query_memberships

import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipCancelledEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipPausedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipReactivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipResumedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipSuspendedEvent
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.springframework.stereotype.Component

@Component
class MembershipProjection(private val membershipRepository: MembershipRepository) {

    @EventHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipRepository.save(
            MembershipEntity(
                evt.membershipId.value,
                evt.customerId.value,
                evt.planTerms.planReferenceId.value,
                evt.planTerms.duration.value,
                evt.planTerms.price.value,
                evt.customerEligibility.dateOfBirth,
                evt.customerEligibility.guardianSignaturePresent,
                MembershipStatus.ACTIVE.name
            )
        )
    }

    @EventHandler
    fun on(evt: MembershipPausedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.PAUSED.name
            it.pauseStartDate = evt.pausePeriod.startDate
            it.pauseEndDate = evt.pausePeriod.endDate
            it.pauseDurationDays = evt.pausePeriod.durationDays
            membershipRepository.save(it)
        }
    }

    @EventHandler
    fun on(evt: MembershipReactivatedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.ACTIVE.name
            membershipRepository.save(it)
        }
    }

    @EventHandler
    fun on(evt: MembershipResumedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.ACTIVE.name
            it.clearPausePeriod()
            membershipRepository.save(it)
        }
    }

    @EventHandler
    fun on(evt: MembershipSuspendedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.SUSPENDED.name
            membershipRepository.save(it)
        }
    }

    @EventHandler
    fun on(evt: MembershipCancelledEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.CANCELLED.name
            it.clearPausePeriod()
            membershipRepository.save(it)
        }
    }

    private fun MembershipEntity.clearPausePeriod() {
        pauseStartDate = null
        pauseEndDate = null
        pauseDurationDays = null
    }
}
