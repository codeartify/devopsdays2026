package com.codeartify.membership.managingmemberships.integration

import com.fasterxml.jackson.databind.JsonNode

data class IntegrationEvent(
    val type: String,
    val version: Int,
    val payload: JsonNode
)
