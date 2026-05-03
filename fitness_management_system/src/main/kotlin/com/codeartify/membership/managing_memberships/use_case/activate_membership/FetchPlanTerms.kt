package com.codeartify.membership.managing_memberships.use_case.activate_membership

import com.codeartify.membership.managing_memberships.domain.values.PlanReferenceId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms

interface FetchPlanTerms {
    fun currentTermsFor(planReferenceId: PlanReferenceId): PlanTerms?
}
