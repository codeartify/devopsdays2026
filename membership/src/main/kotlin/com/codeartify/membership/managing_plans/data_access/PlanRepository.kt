package com.codeartify.membership.managing_plans.data_access

import com.codeartify.membership.managing_plans.domain.Plan
import com.codeartify.membership.managing_plans.domain.values.PlanDuration
import org.springframework.data.jpa.repository.JpaRepository

interface PlanRepository : JpaRepository<Plan, String> {
    fun existsByDurationAndIdNot(duration: PlanDuration, id: String): Boolean
    fun findAllByOrderByDurationAsc(): List<Plan>
}
