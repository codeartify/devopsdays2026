package com.codeartify.membership.contexts.managingmemberships.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class PlanDuration private constructor(@JsonValue val value: Int) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: Int): PlanDuration {
            require(value > 0) { "Plan duration must be greater than zero" }
            return PlanDuration(value)
        }
    }
}
