package com.codeartify.customerservice.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class CustomerAggregate() {

    @AggregateIdentifier
    lateinit var customerId: String

    lateinit var name: String

    @CommandHandler
    constructor(cmd: RegisterCustomerCommand) : this() {
        require(cmd.customerId.isNotBlank()) { "Customer ID must not be blank" }
        require(cmd.name.isNotBlank()) { "Name must not be blank" }

        AggregateLifecycle.apply(CustomerRegisteredEvent(cmd.customerId, cmd.name))
    }


    @EventSourcingHandler
    fun on(evt: CustomerRegisteredEvent) {
        customerId = evt.customerId
        name = evt.name
    }

}
