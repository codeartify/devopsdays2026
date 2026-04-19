package com.codeartify.customerservice.command

data class CustomerRegisteredEvent(
    val customerId: String,
    val name: String
)
