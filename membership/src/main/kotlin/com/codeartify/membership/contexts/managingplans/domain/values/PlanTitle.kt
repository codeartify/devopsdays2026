package com.codeartify.membership.contexts.managingplans.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

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

@Converter(autoApply = true)
class PlanTitleConverter : AttributeConverter<PlanTitle, String> {
    override fun convertToDatabaseColumn(attribute: PlanTitle?): String? = attribute?.value

    override fun convertToEntityAttribute(dbData: String?): PlanTitle? = dbData?.let { PlanTitle.of(it) }
}
