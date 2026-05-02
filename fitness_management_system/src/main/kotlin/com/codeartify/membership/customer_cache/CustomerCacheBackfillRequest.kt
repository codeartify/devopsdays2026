package com.codeartify.membership.customer_cache

import java.time.LocalDate

data class CustomerCacheBackfillRequest(
    val id: String,
    val name: String,
    val email: String,
    val dateOfBirth: LocalDate
)
