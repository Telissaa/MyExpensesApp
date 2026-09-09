package pl.wluczak.myexpenses.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.wluczak.myexpenses.data.ExpenseRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(
    private val repository: ExpenseRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val currentYearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
            val pattern = "$currentYearMonth%"
            repository.getTotalSpentForMonth(pattern).collect { sum ->
                val totalSpent = sum ?: 0.0
                _uiState.update { currentState ->
                    currentState.copy(
                        totalSpent = totalSpent,
                        balance = currentState.totalBudget - totalSpent,
                    )
                }
            }
        }
    }
}