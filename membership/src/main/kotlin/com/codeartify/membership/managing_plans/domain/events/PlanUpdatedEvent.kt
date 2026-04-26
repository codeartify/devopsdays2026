package com.codeartify.membership.managing_plans.domain.events

import com.codeartify.membership.managing_plans.domain.values.PlanId
import org.axonframework.messaging.eventhandling.annotation.Event

@Event(version = "1.0")
data class PlanUpdatedEvent(
    val planId: PlanId,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
