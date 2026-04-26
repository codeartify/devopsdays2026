package com.codeartify.membership.managing_memberships.adapter.data_access

import com.codeartify.membership.managing_memberships.domain.values.PlanDuration
import com.codeartify.membership.managing_memberships.domain.values.PlanId
import com.codeartify.membership.managing_memberships.domain.values.PlanPrice
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import com.codeartify.membership.managing_memberships.use_cases.activate_membership.FetchPlanTerms
import com.codeartify.membership.managing_plans.data_access.PlanRepository
import org.springframework.stereotype.Component

@Component
class PlanTermsProvider(
    private val planRepository: PlanRepository
) : FetchPlanTerms {

    override fun currentTermsFor(planId: PlanId): PlanTerms? =
        planRepository.findById(planId.value)
            .map { plan ->
                PlanTerms(
                    planId = planId,
                    duration = PlanDuration.of(plan.duration.value),
                    price = PlanPrice.of(plan.price.value)
                )
            }
            .orElse(null)
}
