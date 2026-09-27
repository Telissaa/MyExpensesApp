package pl.wluczak.myexpenses.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.wluczak.myexpenses.data.ExpenseRepository
import pl.wluczak.myexpenses.ui.history.HistoryUiState

class HistoryViewModel(private val repository: ExpenseRepository): ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    private val _sortOrder = MutableStateFlow(SortOrder.DATE_DESC)

    fun onSortOrderChanged(newSortOrder: SortOrder) {
        _sortOrder.value = newSortOrder
    }

    init{
        viewModelScope.launch {
            combine(repository.getAllExpenses(),_sortOrder) {
                expensesList, sortOrder ->
            val sortedList = when(sortOrder) {
                SortOrder.DATE_DESC -> expensesList.sortedByDescending { it.date }
                SortOrder.DATE_ASC -> expensesList.sortedBy { it.date }
                SortOrder.AMOUNT_DESC -> expensesList.sortedByDescending { it.amount }
                SortOrder.AMOUNT_ASC -> expensesList.sortedBy { it.amount }
                SortOrder.CATEGORY_ASC -> expensesList.sortedBy { it.category }
                SortOrder.CATEGORY_DESC -> expensesList.sortedByDescending { it.category }
                SortOrder.ALFABETICALLY_ASC -> expensesList.sortedBy { it.name }
            }
            Pair(sortedList, sortOrder)
        }.collect { (finalSortedList, finalSortOrder) ->
            _uiState.value = _uiState.value.copy(
                expenses = finalSortedList,
                currentSortOrder = finalSortOrder,
                isLoading = false
                )
            }
        }
    }
}