package com.codeartify.membership.managingmemberships.query

import com.codeartify.managingmemberships.domain.MembershipActivatedEvent
import com.codeartify.managingmemberships.domain.MembershipPausedEvent
import com.codeartify.managingmemberships.domain.MembershipReactivatedEvent
import com.codeartify.managingmemberships.domain.MembershipStatus
import com.codeartify.managingmemberships.domain.MembershipSuspendedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("membership-projection")
class MembershipProjection(private val membershipRepository: MembershipRepository) {

    @EventHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipRepository.save(
            MembershipEntity(evt.membershipId.value, evt.customerId.value, evt.planId.value, MembershipStatus.ACTIVE.name)
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
