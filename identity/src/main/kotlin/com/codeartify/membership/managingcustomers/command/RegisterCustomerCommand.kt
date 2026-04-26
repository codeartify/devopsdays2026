package com.codeartify.membership.managingcustomers.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

import java.time.LocalDate

data class RegisterCustomerCommand(
    @TargetAggregateIdentifier val customerId: String,
    val name: String,
    val dateOfBirth: LocalDate
)
