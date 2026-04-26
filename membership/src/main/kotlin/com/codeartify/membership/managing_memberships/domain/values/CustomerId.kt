package com.codeartify.membership.managing_memberships.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class CustomerId private constructor(@JsonValue val value: String) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: String): CustomerId {
            requireNotNull(value) { "Customer ID must not be null" }
            try {
                UUID.fromString(value)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Customer ID must be a valid UUID")
            }
            return CustomerId(value)
        }
    }
}
