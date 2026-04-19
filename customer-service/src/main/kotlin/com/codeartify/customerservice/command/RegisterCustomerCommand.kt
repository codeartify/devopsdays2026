package com.codeartify.customerservice.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterCustomerCommand(
    @TargetAggregateIdentifier val customerId: String,
    val name: String
)
