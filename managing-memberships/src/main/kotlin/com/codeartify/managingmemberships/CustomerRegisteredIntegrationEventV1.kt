package com.codeartify.managingmemberships

import java.time.LocalDate

data class CustomerRegisteredIntegrationEventV1(val customerId: String, val name: String, val dateOfBirth: LocalDate)
