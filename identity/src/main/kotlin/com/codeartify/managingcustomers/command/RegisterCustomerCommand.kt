package com.codeartify.managingcustomers.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

import java.time.LocalDate

data class RegisterCustomerCommand(
    @TargetAggregateIdentifier val customerId: String,
    val name: String,
    val email: String,
    val dateOfBirth: LocalDate
)
