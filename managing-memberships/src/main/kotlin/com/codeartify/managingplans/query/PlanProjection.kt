package com.codeartify.managingplans.query

import com.codeartify.managingplans.domain.PlanCreatedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("plan-projection")
class PlanProjection(private val planRepository: PlanRepository) {

    @EventHandler
    fun on(evt: PlanCreatedEvent) {
        planRepository.save(
            PlanEntity(
                evt.planId.value,
                evt.title.value,
                evt.description.value,
                evt.price.value,
                evt.duration.value
            )
        )
    }
}
