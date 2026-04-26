package com.codeartify.membership.managing_memberships.use_cases.activate_membership

import com.codeartify.membership.managing_memberships.domain.values.PlanId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms

interface FetchPlanTerms {
    fun currentTermsFor(planId: PlanId): PlanTerms?
}
