package com.codeartify.managingplans.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class PlanDescription private constructor(@JsonValue val value: String) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: String): PlanDescription {
            require(value.isNotBlank()) { "Plan description must not be blank" }
            return PlanDescription(value.trim())
        }
    }
}
