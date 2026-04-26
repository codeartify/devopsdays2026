package com.codeartify.managingcustomers

import java.time.LocalDate

data class RegisterCustomerRequest(
    val name: String,
    val email: String,
    val dateOfBirth: LocalDate
)
