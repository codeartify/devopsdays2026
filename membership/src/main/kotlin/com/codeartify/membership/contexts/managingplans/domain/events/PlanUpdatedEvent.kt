package com.codeartify.membership.contexts.managingplans.domain.events

import com.codeartify.membership.contexts.managingplans.domain.values.PlanId


data class PlanUpdatedEvent(
    val planId: PlanId,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
