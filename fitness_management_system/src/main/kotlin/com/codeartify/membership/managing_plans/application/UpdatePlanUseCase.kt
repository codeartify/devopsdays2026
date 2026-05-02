package com.codeartify.membership.managing_plans.application

import com.codeartify.membership.managing_plans.data_access.PlanRepository
import com.codeartify.membership.managing_plans.domain.PlanDescription
import com.codeartify.membership.managing_plans.domain.PlanDuration
import com.codeartify.membership.managing_plans.domain.PlanId
import com.codeartify.membership.managing_plans.domain.PlanPrice
import com.codeartify.membership.managing_plans.domain.PlanTitle
import com.codeartify.membership.managing_plans.domain.PlanUpdatedEvent
import org.axonframework.messaging.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdatePlanUseCase(
    private val planRepository: PlanRepository,
    private val eventGateway: EventGateway
) {
    @Transactional
    fun execute(planId: String, request: UpdatePlanRequest) {
        val existingPlanId = PlanId.of(planId)
        val title = PlanTitle.of(request.title)
        val description = PlanDescription.of(request.description)
        val price = PlanPrice.of(request.price)
        val duration = PlanDuration.of(request.durationInMonths)

        require(!planRepository.existsByDurationAndIdNot(duration, existingPlanId.value)) {
            "Plan with duration ${duration.value} months already exists"
        }

        val plan = planRepository.findById(existingPlanId.value)
            .orElseThrow { IllegalArgumentException("Plan with ID ${existingPlanId.value} not found") }

        plan.update(title, description, price, duration)
        planRepository.save(plan)

        eventGateway.publish(
            listOf(
                PlanUpdatedEvent(
                plan.planId,
                plan.title.value,
                plan.description.value,
                plan.price.value,
                plan.duration.value
            )
            )
        )
    }
}
