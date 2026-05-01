package com.codeartify.membership.managing_memberships.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class PlanReferenceId private constructor(@JsonValue val value: String) {
    companion object {
        fun generate() = PlanReferenceId(UUID.randomUUID().toString())

        @JsonCreator
        @JvmStatic
        fun of(value: String): PlanReferenceId {
            requireNotNull(value) { "Plan ID must not be null" }
            try {
                UUID.fromString(value)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Plan ID must be a valid UUID")
            }
            return PlanReferenceId(value)
        }
    }
}
