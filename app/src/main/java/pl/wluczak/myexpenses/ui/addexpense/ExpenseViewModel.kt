package pl.wluczak.myexpenses.ui.addexpense

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.wluczak.myexpenses.ui.home.HomeUiState

class ExpenseViewModel : ViewModel() {

    // _uiState jest prywatne, żeby nikt z zewnątrz nie mógł go zmieniać (bezpieczeństwo!)
    private val _uiState = MutableStateFlow(HomeUiState())

    // uiState jest publiczne, tylko do odczytu dla widoku
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // W przyszłości tutaj zawołamy Repozytorium, które pobierze dane z bazy
        loadDashboardData()
    }

    private fun loadDashboardData() {
        // Symulujemy pobranie danych z bazy
        _uiState.value = HomeUiState(
            totalBudget = 5000.0,
            totalSpent = 1245.50,
            balance = 3754.50,
        )
    }
}