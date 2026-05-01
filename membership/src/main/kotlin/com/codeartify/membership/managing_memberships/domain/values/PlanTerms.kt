package com.codeartify.membership.managing_memberships.domain.values

data class PlanTerms(
    val planReferenceId: PlanReferenceId,
    val duration: Duration,
    val price: Price
)
