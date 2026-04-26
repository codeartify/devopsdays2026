package com.codeartify.membership.contexts.managingmemberships.domain.values

data class PlanTerms(
    val planId: PlanId,
    val duration: PlanDuration,
    val price: PlanPrice
)
