package pl.wluczak.myexpenses.utils

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateRangeTest {
    @Test
    fun dayRangeIncludesOnlyReferenceDate() {
        val range = dateRange(DateRangePeriod.DAY, LocalDate(2024, 5, 15))

        assertEquals(LocalDate(2024, 5, 15), range.startInclusive)
        assertEquals(LocalDate(2024, 5, 16), range.endExclusive)
        assertTrue(range.containsIsoDate("2024-05-15"))
        assertFalse(range.containsIsoDate("2024-05-16"))
    }

    @Test
    fun weekRangeRunsMondayThroughSunday() {
        val range = dateRange(DateRangePeriod.WEEK, LocalDate(2024, 5, 15))

        assertEquals(LocalDate(2024, 5, 13), range.startInclusive)
        assertEquals(LocalDate(2024, 5, 20), range.endExclusive)
        assertTrue(range.contains(LocalDate(2024, 5, 19)))
        assertFalse(range.contains(LocalDate(2024, 5, 20)))
    }

    @Test
    fun weekRangeCrossesNewYearAccordingToIsoWeek() {
        val range = dateRange(DateRangePeriod.WEEK, LocalDate(2022, 1, 1))

        assertEquals(LocalDate(2021, 12, 27), range.startInclusive)
        assertEquals(LocalDate(2022, 1, 3), range.endExclusive)
    }

    @Test
    fun monthRangeUsesCalendarMonthBoundaries() {
        val range = dateRange(DateRangePeriod.MONTH, LocalDate(2024, 5, 15))

        assertEquals(LocalDate(2024, 5, 1), range.startInclusive)
        assertEquals(LocalDate(2024, 6, 1), range.endExclusive)
    }

    @Test
    fun leapFebruaryRangeIncludesFebruaryTwentyNinth() {
        val range = dateRange(DateRangePeriod.MONTH, LocalDate(2024, 2, 29))

        assertEquals(LocalDate(2024, 2, 1), range.startInclusive)
        assertEquals(LocalDate(2024, 3, 1), range.endExclusive)
        assertTrue(range.containsIsoDate("2024-02-29"))
        assertFalse(range.containsIsoDate("2024-03-01"))
    }

    @Test
    fun invalidIsoDateDoesNotMatchRange() {
        val range = dateRange(DateRangePeriod.DAY, LocalDate(2024, 5, 15))

        assertFalse(range.containsIsoDate("not-a-date"))
    }
}
