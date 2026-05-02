package com.codeartify.membership.managing_memberships.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class Price private constructor(@JsonValue val value: Int) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: Int): Price {
            require(value > 0) { "Plan price must be greater than zero" }
            return Price(value)
        }
    }
}
