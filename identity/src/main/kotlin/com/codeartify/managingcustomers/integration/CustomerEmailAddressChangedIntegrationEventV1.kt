package com.codeartify.managingcustomers.integration

data class CustomerEmailAddressChangedIntegrationEventV1(
    val customerId: String,
    val email: String
)
