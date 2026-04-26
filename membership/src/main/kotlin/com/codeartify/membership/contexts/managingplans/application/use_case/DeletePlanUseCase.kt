package com.codeartify.membership.contexts.managingplans.application.use_case

import com.codeartify.membership.contexts.managingplans.data_access.PlanRepository
import com.codeartify.membership.contexts.managingplans.domain.events.PlanDeletedEvent
import com.codeartify.membership.contexts.managingplans.domain.values.PlanId
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DeletePlanUseCase(
    private val planRepository: PlanRepository,
    private val eventGateway: EventGateway
) {
    @Transactional
    fun execute(planId: String) {
        val existingPlanId = PlanId.of(planId)
        val plan = planRepository.findById(existingPlanId.value)
            .orElseThrow { IllegalArgumentException("Plan with ID ${existingPlanId.value} not found") }

        planRepository.delete(plan)
        eventGateway.publish(PlanDeletedEvent(plan.planId))
    }
}
