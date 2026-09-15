package pl.wluczak.myexpenses.ui.history

import pl.wluczak.myexpenses.data.Expense

data class HistoryUiState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)