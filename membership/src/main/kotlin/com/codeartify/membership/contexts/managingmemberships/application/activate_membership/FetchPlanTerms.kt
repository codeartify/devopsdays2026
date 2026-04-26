package com.codeartify.membership.contexts.managingmemberships.application.activate_membership

import com.codeartify.membership.contexts.managingmemberships.domain.PlanId
import com.codeartify.membership.contexts.managingmemberships.domain.PlanTerms

interface FetchPlanTerms {
    fun currentTermsFor(planId: PlanId): PlanTerms?
}
