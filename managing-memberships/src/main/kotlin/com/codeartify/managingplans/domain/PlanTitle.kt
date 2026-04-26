package com.codeartify.managingplans.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class PlanTitle private constructor(@JsonValue val value: String) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: String): PlanTitle {
            require(value.isNotBlank()) { "Plan title must not be blank" }
            return PlanTitle(value.trim())
        }
    }
}
