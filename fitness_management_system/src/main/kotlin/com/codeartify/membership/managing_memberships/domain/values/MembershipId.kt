package com.codeartify.membership.managing_memberships.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class MembershipId private constructor(@JsonValue val value: String) {
    override fun toString(): String = value

    companion object {

        fun generate() = MembershipId(UUID.randomUUID().toString())

        @JsonCreator
        @JvmStatic
        fun of(value: String) = MembershipId(value)
    }
}
