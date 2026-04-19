package com.codeartify.managingcustomers.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterCustomerCommand(
    @TargetAggregateIdentifier val customerId: String,
    val name: String
)
