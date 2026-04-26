package com.codeartify.membership.contexts.managingplans.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

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

@Converter(autoApply = true)
class PlanDurationConverter : AttributeConverter<PlanDuration, Int> {
    override fun convertToDatabaseColumn(attribute: PlanDuration?): Int? = attribute?.value

    override fun convertToEntityAttribute(dbData: Int?): PlanDuration? = dbData?.let { PlanDuration.of(it) }
}
