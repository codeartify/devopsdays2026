package com.codeartify.membership.managingmemberships.integration

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import java.time.LocalDate

data class CustomerRegisteredIntegrationEventV1(
    val customerId: String,
    val name: String,
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val dateOfBirth: LocalDate
)
