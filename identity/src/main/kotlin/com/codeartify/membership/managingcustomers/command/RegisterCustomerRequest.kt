package com.codeartify.membership.managingcustomers.command

import java.time.LocalDate

data class RegisterCustomerRequest(
    val name: String,
    val dateOfBirth: LocalDate
)
