package com.codeartify.membership.managing_memberships.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class Duration private constructor(@JsonValue val value: Int) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: Int): Duration {
            require(value > 0) { "Plan duration must be greater than zero" }
            return Duration(value)
        }
    }
}
