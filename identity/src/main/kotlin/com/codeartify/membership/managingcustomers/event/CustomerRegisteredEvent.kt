package com.codeartify.membership.managingcustomers.event

import org.axonframework.serialization.Revision
import java.time.LocalDate

@Revision("1.0")
data class CustomerRegisteredEvent(
    val customerId: String,
    val name: String,
    val dateOfBirth: LocalDate
)
