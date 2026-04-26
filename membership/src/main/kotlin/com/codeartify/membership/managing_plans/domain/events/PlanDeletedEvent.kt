package com.codeartify.membership.managing_plans.domain.events

import com.codeartify.membership.managing_plans.domain.values.PlanId


data class PlanDeletedEvent(
    val planId: PlanId
)
