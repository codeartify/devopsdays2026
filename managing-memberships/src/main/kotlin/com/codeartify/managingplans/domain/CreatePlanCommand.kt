package com.codeartify.managingplans.domain

import com.codeartify.managingmemberships.domain.PlanId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreatePlanCommand(
    @TargetAggregateIdentifier val planId: PlanId,
    val title: PlanTitle,
    val description: PlanDescription,
    val price: PlanPrice,
    val duration: PlanDuration
)
