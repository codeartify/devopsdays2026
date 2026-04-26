package com.codeartify.membership.managingmemberships.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

data class PlanId private constructor(@JsonValue val value: String) {
    companion object {
        fun generate() = PlanId(UUID.randomUUID().toString())

        @JsonCreator
        @JvmStatic
        fun of(value: String): PlanId {
            requireNotNull(value) { "Plan ID must not be null" }
            try {
                UUID.fromString(value)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Plan ID must be a valid UUID")
            }
            return PlanId(value)
        }
    }
}
