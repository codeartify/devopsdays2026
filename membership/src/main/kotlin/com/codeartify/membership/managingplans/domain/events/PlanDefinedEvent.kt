package com.codeartify.membership.managingplans.domain.events

import com.codeartify.managingplans.domain.values.PlanId


data class PlanDefinedEvent(
    val planId: PlanId,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
