package com.codeartify.managingcustomers.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDate

@Aggregate
class Customer() {

    @AggregateIdentifier
    lateinit var customerId: String
    lateinit var name: String
    lateinit var dateOfBirth: LocalDate

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
   fun register(cmd: RegisterCustomerCommand) {
        require(cmd.customerId.isNotBlank()) { "Customer ID must not be blank" }
        require(cmd.name.isNotBlank()) { "Name must not be blank" }
        require(cmd.dateOfBirth.isBefore(LocalDate.now().minusYears(16).plusDays(1))) { "Customer must be at least 16 years old" }

        apply(
            CustomerRegisteredEvent(
                cmd.customerId,
                cmd.name,
                cmd.dateOfBirth
            )
        )
    }


    @EventSourcingHandler
    fun on(evt: CustomerRegisteredEvent) {
        customerId = evt.customerId
        name = evt.name
        dateOfBirth = evt.dateOfBirth
    }

}
