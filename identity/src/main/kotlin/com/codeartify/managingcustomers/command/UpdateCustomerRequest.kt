package com.codeartify.managingcustomers.command

import java.time.LocalDate

data class UpdateCustomerRequest(
    val name: String,
    val email: String,
    val dateOfBirth: LocalDate
)
