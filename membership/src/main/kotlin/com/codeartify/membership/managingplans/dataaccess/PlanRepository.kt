package com.codeartify.membership.managingplans.dataaccess

import com.codeartify.managingplans.domain.Plan
import com.codeartify.managingplans.domain.values.PlanDuration
import org.springframework.data.jpa.repository.JpaRepository

interface PlanRepository : JpaRepository<Plan, String> {
    fun existsByDuration(duration: PlanDuration): Boolean
    fun existsByDurationAndIdNot(duration: PlanDuration, id: String): Boolean
    fun findAllByOrderByDurationAsc(): List<Plan>
}
