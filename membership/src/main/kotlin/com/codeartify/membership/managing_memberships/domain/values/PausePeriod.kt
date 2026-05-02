package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate
import java.time.temporal.ChronoUnit

private fun LocalDate.isInTheFuture(): Boolean =
    !isBefore(LocalDate.now())

data class PausePeriod(
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    init {
        require(startDate.isInTheFuture()) {
            "Pause start date must be in the future"
        }
        require(endDate.isAfter(startDate)) {
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
