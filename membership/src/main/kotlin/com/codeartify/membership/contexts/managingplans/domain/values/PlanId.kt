package com.codeartify.membership.contexts.managingplans.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.UUID

@ConsistentCopyVisibility
data class PlanId private constructor(@JsonValue val value: String) {
    companion object {
        fun generate() = PlanId(UUID.randomUUID().toString())

        @JsonCreator
        @JvmStatic
        fun of(value: String): PlanId {
            requireNotNull(value) { "Plan ID must not be null" }
            try {
                UUID.fromString(value)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Plan ID must be a valid UUID")
            }
            return PlanId(value)
        }
    }
}

@Converter(autoApply = true)
class PlanIdConverter : AttributeConverter<PlanId, String> {
    override fun convertToDatabaseColumn(attribute: PlanId?): String? = attribute?.value

    override fun convertToEntityAttribute(dbData: String?): PlanId? = dbData?.let { PlanId.of(it) }
}
