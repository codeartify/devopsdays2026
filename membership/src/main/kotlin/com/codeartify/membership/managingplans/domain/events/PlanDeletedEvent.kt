package com.codeartify.membership.managingplans.domain.events

import com.codeartify.managingplans.domain.values.PlanId


data class PlanDeletedEvent(
    val planId: PlanId
)
