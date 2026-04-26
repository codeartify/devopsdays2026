package com.codeartify.membership.contexts.managingmemberships.use_cases.activate_membership

import com.codeartify.membership.contexts.managingmemberships.domain.values.PlanId
import com.codeartify.membership.contexts.managingmemberships.domain.values.PlanTerms

interface FetchPlanTerms {
    fun currentTermsFor(planId: PlanId): PlanTerms?
}
