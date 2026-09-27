package pl.wluczak.myexpenses.ui.history

import pl.wluczak.myexpenses.data.Expense

enum class SortOrder {
    DATE_DESC,
    DATE_ASC,
    AMOUNT_DESC,
    AMOUNT_ASC,
    CATEGORY_ASC,
    CATEGORY_DESC,
    ALFABETICALLY_ASC,
}

data class HistoryUiState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentSortOrder: SortOrder = SortOrder.DATE_DESC
)