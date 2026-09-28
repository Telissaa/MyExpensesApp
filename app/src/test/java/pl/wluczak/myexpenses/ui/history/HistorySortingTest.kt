package pl.wluczak.myexpenses.ui.history

import pl.wluczak.myexpenses.data.Expense
import org.junit.Assert.assertEquals
import org.junit.Test

class HistorySortingTest {
    private val sameDayExpenses = listOf(
        expense(id = 1, date = "2026-09-28", name = "First"),
        expense(id = 3, date = "2026-09-28", name = "Third"),
        expense(id = 2, date = "2026-09-28", name = "Second"),
        expense(id = 4, date = "2026-09-27", name = "Previous day")
    )

    @Test
    fun newestFirstUsesDescendingIdWithinSameDay() {
        val sorted = sortExpenses(sameDayExpenses, SortOrder.DATE_DESC)

        assertEquals(listOf(3, 2, 1, 4), sorted.map { it.id })
    }

    @Test
    fun oldestFirstUsesAscendingIdWithinSameDay() {
        val sorted = sortExpenses(sameDayExpenses, SortOrder.DATE_ASC)

        assertEquals(listOf(4, 1, 2, 3), sorted.map { it.id })
    }

    private fun expense(id: Int, date: String, name: String) = Expense(
        id = id,
        name = name,
        amount = id.toDouble(),
        date = date,
        category = "Test"
    )
}
