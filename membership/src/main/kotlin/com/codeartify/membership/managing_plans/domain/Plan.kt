package com.codeartify.membership.managing_plans.domain

import com.codeartify.membership.managing_plans.domain.values.PlanDescription
import com.codeartify.membership.managing_plans.domain.values.PlanDescriptionConverter
import com.codeartify.membership.managing_plans.domain.values.PlanDuration
import com.codeartify.membership.managing_plans.domain.values.PlanDurationConverter
import com.codeartify.membership.managing_plans.domain.values.PlanId
import com.codeartify.membership.managing_plans.domain.values.PlanPrice
import com.codeartify.membership.managing_plans.domain.values.PlanPriceConverter
import com.codeartify.membership.managing_plans.domain.values.PlanTitle
import com.codeartify.membership.managing_plans.domain.values.PlanTitleConverter
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Transient

@Entity
@Table(name = "plans")
class Plan() {

    @Id
    lateinit var id: String

    @get:Transient
    val planId: PlanId
        get() = PlanId.of(id)

    @Convert(converter = PlanTitleConverter::class)
    lateinit var title: PlanTitle

    @Convert(converter = PlanDescriptionConverter::class)
    lateinit var description: PlanDescription

    @Convert(converter = PlanPriceConverter::class)
    lateinit var price: PlanPrice

    @Convert(converter = PlanDurationConverter::class)
    lateinit var duration: PlanDuration

    fun update(title: PlanTitle, description: PlanDescription, price: PlanPrice, duration: PlanDuration) {
        this.title = title
        this.description = description
        this.price = price
        this.duration = duration
    }

    companion object {
        fun create(
            planId: PlanId,
            title: PlanTitle,
            description: PlanDescription,
            price: PlanPrice,
            duration: PlanDuration
        ): Plan {
            val plan = Plan()
            plan.id = planId.value
            plan.title = title
            plan.description = description
            plan.price = price
            plan.duration = duration
            return plan
        }
    }
}
