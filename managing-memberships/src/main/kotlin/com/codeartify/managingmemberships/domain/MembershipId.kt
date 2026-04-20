package com.codeartify.managingmemberships.domain

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.UUID

data class MembershipId private constructor(val value: String) {
    companion object {
        fun generate() = MembershipId(UUID.randomUUID().toString())

        @JsonCreator
        @JvmStatic
        fun of(value: String) = MembershipId(value)
    }
}
