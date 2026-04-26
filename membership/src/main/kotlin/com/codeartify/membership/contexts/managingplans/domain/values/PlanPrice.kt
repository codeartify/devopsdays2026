package com.codeartify.membership.contexts.managingplans.domain.values

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

data class PlanPrice private constructor(@JsonValue val value: Int) {
    companion object {
        @JsonCreator
        @JvmStatic
        fun of(value: Int): PlanPrice {
            require(value > 0) { "Plan price must be greater than zero" }
            return PlanPrice(value)
        }
    }
}

@Converter(autoApply = true)
class PlanPriceConverter : AttributeConverter<PlanPrice, Int> {
    override fun convertToDatabaseColumn(attribute: PlanPrice?): Int? = attribute?.value

    override fun convertToEntityAttribute(dbData: Int?): PlanPrice? = dbData?.let { PlanPrice.of(it) }
}
