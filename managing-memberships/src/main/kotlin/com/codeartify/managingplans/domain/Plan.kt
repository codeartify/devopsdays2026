package com.codeartify.managingplans.domain

import com.codeartify.managingmemberships.domain.PlanId
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Plan() {

    @AggregateIdentifier
    lateinit var planId: PlanId
    lateinit var title: PlanTitle
    lateinit var description: PlanDescription
    lateinit var price: PlanPrice
    lateinit var duration: PlanDuration

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    fun create(cmd: CreatePlanCommand) {
        apply(PlanCreatedEvent(cmd.planId, cmd.title, cmd.description, cmd.price, cmd.duration))
    }

    @EventSourcingHandler
    fun on(evt: PlanCreatedEvent) {
        planId = evt.planId
        title = evt.title
        description = evt.description
        price = evt.price
        duration = evt.duration
    }
}
