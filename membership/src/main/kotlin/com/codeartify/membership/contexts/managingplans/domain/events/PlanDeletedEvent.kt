package com.codeartify.membership.contexts.managingplans.domain.events

import com.codeartify.membership.contexts.managingplans.domain.values.PlanId


data class PlanDeletedEvent(
    val planId: PlanId
)
