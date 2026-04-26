package com.codeartify.membership.managingplans.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

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

@Converter(autoApply = true)
class PlanDescriptionConverter : AttributeConverter<PlanDescription, String> {
    override fun convertToDatabaseColumn(attribute: PlanDescription?): String? = attribute?.value

    override fun convertToEntityAttribute(dbData: String?): PlanDescription? = dbData?.let { PlanDescription.of(it) }
}
