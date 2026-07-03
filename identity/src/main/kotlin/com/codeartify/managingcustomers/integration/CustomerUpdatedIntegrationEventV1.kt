package com.codeartify.managingcustomers.integration

import java.time.LocalDate

data class CustomerUpdatedIntegrationEventV1(
    val customerId: String,
    val name: String,
    val email: String,
    val dateOfBirth: LocalDate
)
