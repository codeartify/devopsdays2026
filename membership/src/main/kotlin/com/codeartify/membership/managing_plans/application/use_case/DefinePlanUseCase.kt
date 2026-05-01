package com.codeartify.membership.managing_plans.application.use_case

import com.codeartify.membership.managing_plans.application.request.CreatePlanRequest
import com.codeartify.membership.managing_plans.data_access.PlanRepository
import com.codeartify.membership.managing_plans.domain.Plan
import com.codeartify.membership.managing_plans.domain.values.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DefinePlanUseCase(
    private val planRepository: PlanRepository
) {
    @Transactional
    fun execute(request: CreatePlanRequest): PlanId {
        val planId = PlanId.generate()
        val plan = toPlan(request, planId)

        planRepository.save(plan)
        return planId
    }

    private fun toPlan(
        request: CreatePlanRequest,
        planId: PlanId
    ): Plan {
        val title = PlanTitle.of(request.title)
        val description = PlanDescription.of(request.description)
        val price = PlanPrice.of(request.price)
        val duration = PlanDuration.of(request.durationInMonths)
        val plan = Plan.create(planId, title, description, price, duration)
        return plan
    }
}
