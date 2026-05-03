package com.codeartify.membership.managing_memberships.adapter.data_access

import com.codeartify.membership.managing_memberships.domain.values.Duration
import com.codeartify.membership.managing_memberships.domain.values.PlanReferenceId
import com.codeartify.membership.managing_memberships.domain.values.Price
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import com.codeartify.membership.managing_memberships.use_case.activate_membership.FetchPlanTerms
import com.codeartify.membership.managing_plans.data_access.PlanRepository
import org.springframework.stereotype.Component


// Adapter to access another bounded context / possibly other system
@Component
class PlanTermsProvider(
    private val planRepository: PlanRepository
) : FetchPlanTerms {

    // TODO: Implement fetching plan terms from "managing_plans" bounded context's plan repository
    override fun currentTermsFor(planReferenceId: PlanReferenceId): PlanTerms? = null
}
