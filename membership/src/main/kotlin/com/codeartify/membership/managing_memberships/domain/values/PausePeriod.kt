package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PausePeriod(
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    init {
        val durationDays = ChronoUnit.DAYS.between(startDate, endDate).toInt()

        require(durationDays in 30..60) {
            "Pause duration must be between 30 and 60 days"
        }

        require(!endDate.isBefore(startDate)) {
            "Pause end date must not be before start date"
        }
    }

    val durationDays: Int
        get() = ChronoUnit.DAYS.between(startDate, endDate).toInt()
}
