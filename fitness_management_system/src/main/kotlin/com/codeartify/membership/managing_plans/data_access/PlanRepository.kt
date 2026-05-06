package com.codeartify.membership.managing_plans.data_access

import com.codeartify.membership.managing_plans.domain.Plan
import com.codeartify.membership.managing_plans.domain.PlanDuration
import org.springframework.data.jpa.repository.JpaRepository

interface PlanRepository : JpaRepository<Plan, String> {
    fun existsByDuration(duration: PlanDuration): Boolean
    fun findAllByOrderByDurationAsc(): List<Plan>
}
