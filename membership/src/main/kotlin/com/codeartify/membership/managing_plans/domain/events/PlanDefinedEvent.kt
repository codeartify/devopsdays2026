package com.codeartify.membership.managing_plans.domain.events

import com.codeartify.membership.managing_plans.domain.values.PlanId


data class PlanDefinedEvent(
    val planId: PlanId,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
