package pl.wluczak.myexpenses.ui.history

import pl.wluczak.myexpenses.data.Expense

internal fun sortExpenses(expenses: List<Expense>, sortOrder: SortOrder): List<Expense> =
    when (sortOrder) {
        SortOrder.DATE_DESC ->
            expenses.sortedWith(compareByDescending<Expense> { it.date }.thenByDescending { it.id })
        SortOrder.DATE_ASC ->
            expenses.sortedWith(compareBy<Expense> { it.date }.thenBy { it.id })
        SortOrder.AMOUNT_DESC -> expenses.sortedByDescending { it.amount }
        SortOrder.AMOUNT_ASC -> expenses.sortedBy { it.amount }
        SortOrder.CATEGORY_ASC -> expenses.sortedBy { it.category }
        SortOrder.CATEGORY_DESC -> expenses.sortedByDescending { it.category }
        SortOrder.ALPHABETICALLY_ASC -> expenses.sortedBy { it.name }
    }
