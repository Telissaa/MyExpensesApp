package pl.wluczak.myexpenses.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.wluczak.myexpenses.data.ExpenseRepository
import pl.wluczak.myexpenses.ui.history.HistoryUiState

class HistoryViewModel(private val repository: ExpenseRepository): ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch() {
            repository.getAllExpenses().collect { expenses ->
                _uiState.value = _uiState.value.copy(
                    expenses = expenses,
                    isLoading = false,
                    errorMessage = null
                )
            }

        }
    }

}