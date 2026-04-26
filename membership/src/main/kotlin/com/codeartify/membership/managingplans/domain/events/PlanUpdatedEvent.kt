package com.codeartify.membership.managingplans.domain.events

import com.codeartify.managingplans.domain.values.PlanId


data class PlanUpdatedEvent(
    val planId: PlanId,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
