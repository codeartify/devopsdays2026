package com.codeartify.managingplans

import com.codeartify.managingmemberships.domain.PlanId
import com.codeartify.managingplans.domain.CreatePlanCommand
import com.codeartify.managingplans.domain.PlanDescription
import com.codeartify.managingplans.domain.PlanDuration
import com.codeartify.managingplans.domain.PlanPrice
import com.codeartify.managingplans.domain.PlanTitle
import com.codeartify.managingplans.query.PlanRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreatePlanUseCase(
    private val commandGateway: CommandGateway,
    private val planRepository: PlanRepository
) {
    fun execute(request: CreatePlanRequest): PlanId {
        val title = PlanTitle.of(request.title)
        val description = PlanDescription.of(request.description)
        val price = PlanPrice.of(request.price)
        val duration = PlanDuration.of(request.durationInMonths)

        require(!planRepository.existsByDurationInMonths(duration.value)) {
            "Plan with duration ${duration.value} months already exists"
        }

        val planId = PlanId.generate()
        val command = CreatePlanCommand(planId, title, description, price, duration)

        return commandGateway.sendAndWait(command)
    }
}
