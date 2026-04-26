package com.codeartify.membership.contexts.managingmemberships.adapter.data_access

import com.codeartify.membership.contexts.managingmemberships.activate_membership.FetchPlanTerms
import com.codeartify.membership.contexts.managingmemberships.domain.PlanDuration
import com.codeartify.membership.contexts.managingmemberships.domain.PlanId
import com.codeartify.membership.contexts.managingmemberships.domain.PlanPrice
import com.codeartify.membership.contexts.managingmemberships.domain.PlanTerms
import com.codeartify.membership.contexts.managingplans.data_access.PlanRepository
import org.springframework.stereotype.Component

@Component
class PlanTermsProvider(
    private val planRepository: PlanRepository
) : FetchPlanTerms {

    override fun fetch(planId: PlanId): PlanTerms? =
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
