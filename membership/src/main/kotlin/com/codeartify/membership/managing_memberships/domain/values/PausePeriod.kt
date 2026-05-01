package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PausePeriod(
    val startDate: LocalDate,
    val endDate: LocalDate
) {

    val durationDays: Int
        get() = ChronoUnit.DAYS.between(startDate, endDate).toInt()

    companion object {
        fun from(durationInDays: Int): PausePeriod {
            require(durationInDays in 30..60) {
                "Pause duration must be between 30 and 60 days"
            }
            return PausePeriod(
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(durationInDays.toLong())
            )
        }
    }
}
