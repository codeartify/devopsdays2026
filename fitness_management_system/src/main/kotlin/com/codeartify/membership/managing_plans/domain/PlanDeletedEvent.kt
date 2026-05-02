package com.codeartify.membership.managing_plans.domain

import org.axonframework.messaging.eventhandling.annotation.Event

@Event(version = "1.0")
data class PlanDeletedEvent(
    val planId: PlanId
)
