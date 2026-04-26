package com.codeartify.membership.contexts.customer_cache

import com.fasterxml.jackson.databind.JsonNode

data class IntegrationEvent(
    val type: String,
    val version: Int,
    val payload: JsonNode
)
