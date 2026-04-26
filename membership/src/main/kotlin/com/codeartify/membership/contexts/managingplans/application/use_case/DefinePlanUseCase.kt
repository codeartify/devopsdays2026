package com.codeartify.membership.contexts.managingplans.application.use_case

import com.codeartify.membership.contexts.managingplans.application.request.CreatePlanRequest
import com.codeartify.membership.contexts.managingplans.dataaccess.PlanRepository
import com.codeartify.membership.contexts.managingplans.domain.Plan
import com.codeartify.membership.contexts.managingplans.domain.events.PlanDefinedEvent
import com.codeartify.membership.contexts.managingplans.domain.values.PlanDescription
import com.codeartify.membership.contexts.managingplans.domain.values.PlanDuration
import com.codeartify.membership.contexts.managingplans.domain.values.PlanId
import com.codeartify.membership.contexts.managingplans.domain.values.PlanPrice
import com.codeartify.membership.contexts.managingplans.domain.values.PlanTitle
import org.axonframework.eventhandling.gateway.EventGateway
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
            PlanDefinedEvent(
                plan.planId,
                plan.title.value,
                plan.description.value,
                plan.price.value,
                plan.duration.value
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
