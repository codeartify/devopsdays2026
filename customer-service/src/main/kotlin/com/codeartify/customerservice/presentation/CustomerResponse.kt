package com.codeartify.customerservice.presentation

data class CustomerResponse(
    val id: String,
    val name: String,
    val orderIds: List<String> = emptyList()
)
