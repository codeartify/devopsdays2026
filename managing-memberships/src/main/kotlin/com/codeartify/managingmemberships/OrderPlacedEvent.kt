package com.codeartify.managingmemberships

import java.math.BigDecimal

data class OrderPlacedEvent(
    val orderId: String,
    val customerId: String,
    val amount: BigDecimal
)
