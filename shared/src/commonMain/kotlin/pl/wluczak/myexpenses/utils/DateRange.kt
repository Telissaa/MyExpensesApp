package pl.wluczak.myexpenses.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.minus

enum class DateRangePeriod {
    DAY,
    WEEK,
    MONTH
}

data class DateRange(
    val startInclusive: LocalDate,
    val endExclusive: LocalDate
) {
    operator fun contains(date: LocalDate): Boolean =
        date >= startInclusive && date < endExclusive

    fun containsIsoDate(date: String): Boolean =
        try {
            LocalDate.parse(date) in this
        } catch (_: IllegalArgumentException) {
            false
        }
}

fun dateRange(
    period: DateRangePeriod,
    referenceDate: LocalDate
): DateRange = when (period) {
    DateRangePeriod.DAY -> DateRange(
        startInclusive = referenceDate,
        endExclusive = referenceDate + DatePeriod(days = 1)
    )

    DateRangePeriod.WEEK -> {
        val monday = referenceDate - DatePeriod(days = referenceDate.dayOfWeek.isoDayNumber - 1)
        DateRange(
            startInclusive = monday,
            endExclusive = monday + DatePeriod(days = 7)
        )
    }

    DateRangePeriod.MONTH -> {
        val firstDay = LocalDate(referenceDate.year, referenceDate.month, 1)
        DateRange(
            startInclusive = firstDay,
            endExclusive = firstDay + DatePeriod(months = 1)
        )
    }
}
