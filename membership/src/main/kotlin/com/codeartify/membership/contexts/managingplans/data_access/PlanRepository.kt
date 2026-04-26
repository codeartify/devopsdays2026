package com.codeartify.membership.contexts.managingplans.data_access

import com.codeartify.membership.contexts.managingplans.domain.Plan
import com.codeartify.membership.contexts.managingplans.domain.values.PlanDuration
import org.springframework.data.jpa.repository.JpaRepository

interface PlanRepository : JpaRepository<Plan, String> {
    fun existsByDuration(duration: PlanDuration): Boolean
    fun existsByDurationAndIdNot(duration: PlanDuration, id: String): Boolean
    fun findAllByOrderByDurationAsc(): List<Plan>
}
