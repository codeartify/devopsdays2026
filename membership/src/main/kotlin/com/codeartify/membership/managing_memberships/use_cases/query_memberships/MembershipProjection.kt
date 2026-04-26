package com.codeartify.membership.managing_memberships.use_cases.query_memberships

import com.codeartify.membership.managing_memberships.domain.events.MembershipPausedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipReactivatedEvent
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus
import com.codeartify.membership.managing_memberships.domain.events.MembershipSuspendedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("membership-projection")
class MembershipProjection(private val membershipRepository: MembershipRepository) {

    @EventHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipRepository.save(
            MembershipEntity(
                evt.membershipId.value,
                evt.customerId.value,
                evt.planTerms.planId.value,
                MembershipStatus.ACTIVE.name
            )
        )
    }

    @EventHandler
    fun on(evt: MembershipPausedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.PAUSED.name
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
    fun on(evt: MembershipSuspendedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = MembershipStatus.SUSPENDED.name
            membershipRepository.save(it)
        }
    }
}
