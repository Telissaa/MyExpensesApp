package pl.wluczak.myexpenses.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.wluczak.myexpenses.data.ExpenseDao
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(
    private val expenseDao: ExpenseDao
) : ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    init{
        viewModelScope.launch {
            // Tymczasowy test - dodanie wydatku w nowym formacie String (yyyy-MM-dd)
            // Możesz to usunąć, gdy będziesz mieć ekran dodawania wydatków
            expenseDao.insertExpense(
                pl.wluczak.myexpenses.data.Expense(
                    name = "Produkt testowy",
                    amount = 123.45,
                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    category = "Test"
                )
            )

            val currentYearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
            val pattern = "$currentYearMonth%"
            expenseDao.getTotalSpentForMonth(pattern).collect { sum ->
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