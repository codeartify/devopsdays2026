package com.codeartify.membership.contexts.managingmemberships.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

data class MembershipId private constructor(@JsonValue val value: String) {
    companion object {
        fun generate() = MembershipId(UUID.randomUUID().toString())

        @JsonCreator
        @JvmStatic
        fun of(value: String) = MembershipId(value)
    }
}
