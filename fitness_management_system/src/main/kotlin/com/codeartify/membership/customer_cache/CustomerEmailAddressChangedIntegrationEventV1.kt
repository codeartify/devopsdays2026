package com.codeartify.membership.customer_cache

data class CustomerEmailAddressChangedIntegrationEventV1(
    val customerId: String,
    val email: String
)
