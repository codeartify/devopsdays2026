package com.codeartify.managingplans.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class PlanPrice private constructor(@JsonValue val value: Int) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: Int): PlanPrice {
            require(value > 0) { "Plan price must be greater than zero" }
            return PlanPrice(value)
        }
    }
}
