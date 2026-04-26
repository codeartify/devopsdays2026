package com.codeartify.membership.contexts.managingplans.domain.events

import com.codeartify.membership.contexts.managingplans.domain.values.PlanId


data class PlanDefinedEvent(
    val planId: PlanId,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
