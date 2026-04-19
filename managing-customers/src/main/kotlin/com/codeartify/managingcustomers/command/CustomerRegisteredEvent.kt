package com.codeartify.managingcustomers.command

import org.axonframework.serialization.Revision

@Revision("1.0")
data class CustomerRegisteredEvent(
    val customerId: String,
    val name: String
)
