package com.codeartify.managingcustomers.query

import java.time.LocalDate

data class CustomerRegisteredEventV1(val customerId: String, val name: String, val dateOfBirth: LocalDate)
