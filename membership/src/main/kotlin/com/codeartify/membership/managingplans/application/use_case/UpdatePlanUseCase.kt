package com.codeartify.membership.managingplans.application.use_case

import com.codeartify.managingplans.application.request.UpdatePlanRequest
import com.codeartify.managingplans.dataaccess.PlanRepository
import com.codeartify.managingplans.domain.values.PlanDescription
import com.codeartify.managingplans.domain.values.PlanDuration
import com.codeartify.managingplans.domain.values.PlanPrice
import com.codeartify.managingplans.domain.values.PlanTitle
import com.codeartify.managingplans.domain.events.PlanUpdatedEvent
import com.codeartify.managingplans.domain.values.PlanId
import org.axonframework.eventhandling.gateway.EventGateway
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
            PlanUpdatedEvent(
                plan.planId,
                plan.title.value,
                plan.description.value,
                plan.price.value,
                plan.duration.value
            )
        )
    }
}
