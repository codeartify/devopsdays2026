package com.codeartify.membership.contexts.managingmemberships.domain

data class PlanTerms(
    val planId: PlanId,
    val duration: PlanDuration,
    val price: PlanPrice
)
