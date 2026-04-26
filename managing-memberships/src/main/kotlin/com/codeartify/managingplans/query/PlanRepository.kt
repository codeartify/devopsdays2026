package com.codeartify.managingplans.query

import org.springframework.data.jpa.repository.JpaRepository

interface PlanRepository : JpaRepository<PlanEntity, String> {
    fun existsByDurationInMonths(durationInMonths: Int): Boolean
    fun findAllByOrderByDurationInMonthsAsc(): List<PlanEntity>
}
