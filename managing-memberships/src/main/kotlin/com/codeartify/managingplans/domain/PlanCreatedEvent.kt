package com.codeartify.managingplans.domain

import com.codeartify.managingmemberships.domain.PlanId

data class PlanCreatedEvent(
    val planId: PlanId,
    val title: PlanTitle,
    val description: PlanDescription,
    val price: PlanPrice,
    val duration: PlanDuration
)
