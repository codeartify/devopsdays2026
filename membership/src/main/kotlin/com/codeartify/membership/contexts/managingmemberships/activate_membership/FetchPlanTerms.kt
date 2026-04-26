package com.codeartify.membership.contexts.managingmemberships.activate_membership

import com.codeartify.membership.contexts.managingmemberships.domain.PlanId
import com.codeartify.membership.contexts.managingmemberships.domain.PlanTerms

interface FetchPlanTerms {
    fun fetch(planId: PlanId): PlanTerms?
}
