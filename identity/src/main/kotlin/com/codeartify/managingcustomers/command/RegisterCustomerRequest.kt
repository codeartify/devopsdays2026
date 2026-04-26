package com.codeartify.managingcustomers.command

import java.time.LocalDate

data class RegisterCustomerRequest(
    val name: String,
    val dateOfBirth: LocalDate
)
