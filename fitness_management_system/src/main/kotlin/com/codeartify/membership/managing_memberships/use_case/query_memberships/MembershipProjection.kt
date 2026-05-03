package com.codeartify.membership.managing_memberships.use_case.query_memberships

import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.springframework.stereotype.Component

@Component
class MembershipProjection(private val membershipRepository: MembershipRepository) {

    /**
     * # 3 React to the ActivateMembershipEvent
     *
     * * Use @EventHandler on the methods that should react on the event
     *
     * 1. update the local membership projection (used in the ActivateMembershipUseCase to check if the customer already has an
     *    active membership)
     */
    @EventHandler
    fun on(evt: MembershipActivatedEvent) {

    }

}
