package com.codeartify.managingmemberships.query

import com.codeartify.managingmemberships.domain.MembershipActivatedEvent
import com.codeartify.managingmemberships.domain.MembershipPausedEvent
import com.codeartify.managingmemberships.domain.MembershipReactivatedEvent
import com.codeartify.managingmemberships.domain.MembershipSuspendedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class MembershipProjection(private val membershipRepository: MembershipRepository) {

    @EventHandler
    fun on(evt: MembershipActivatedEvent) {
        membershipRepository.save(
            MembershipEntity(evt.membershipId.value, evt.customerId.value, evt.planId.value, "ACTIVE")
        )
    }

    @EventHandler
    fun on(evt: MembershipPausedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = "PAUSED"
            membershipRepository.save(it)
        }
    }

    @EventHandler
    fun on(evt: MembershipReactivatedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = "ACTIVE"
            membershipRepository.save(it)
        }
    }

    @EventHandler
    fun on(evt: MembershipSuspendedEvent) {
        membershipRepository.findById(evt.membershipId.value).ifPresent {
            it.status = "SUSPENDED"
            membershipRepository.save(it)
        }
    }
}
