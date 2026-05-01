package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PausePeriod(
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    init {
        require(!endDate.isBefore(startDate)) {
            "Pause end date must not be before start date"
        }
        require(durationDays in 30..60) {
            "Pause duration must be between 30 and 60 days"
        }
    }

    val durationDays: Int
        get() = ChronoUnit.DAYS.between(startDate, endDate).toInt()

    companion object {
        fun from(durationInDays: Int): PausePeriod {
            return PausePeriod(
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(durationInDays.toLong())
            )
        }
    }
}
