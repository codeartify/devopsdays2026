package com.codeartify.membership.managing_plans.application

import com.codeartify.membership.managing_plans.data_access.PlanRepository
import com.codeartify.membership.managing_plans.domain.Plan
import com.codeartify.membership.managing_plans.domain.PlanDefinedEvent
import com.codeartify.membership.managing_plans.domain.PlanDescription
import com.codeartify.membership.managing_plans.domain.PlanDuration
import com.codeartify.membership.managing_plans.domain.PlanId
import com.codeartify.membership.managing_plans.domain.PlanPrice
import com.codeartify.membership.managing_plans.domain.PlanTitle
import org.axonframework.messaging.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DefinePlanUseCase(
    private val eventGateway: EventGateway,
    private val planRepository: PlanRepository
) {
    @Transactional
    fun execute(request: CreatePlanRequest): PlanId {
        val planId = PlanId.generate()
        val plan = toPlan(request, planId)

        planRepository.save(plan)

        eventGateway.publish(
            listOf(
                PlanDefinedEvent(
                    plan.planId,
                    plan.title.value,
                    plan.description.value,
                    plan.price.value,
                    plan.duration.value
                )
            )
        )

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
