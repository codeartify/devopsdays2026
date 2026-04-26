package com.codeartify.membership.managing_memberships.domain.values

data class PlanTerms(
    val planId: PlanId,
    val duration: PlanDuration,
    val price: PlanPrice
)
