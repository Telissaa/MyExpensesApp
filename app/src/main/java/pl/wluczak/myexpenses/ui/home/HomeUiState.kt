package pl.wluczak.myexpenses.ui.home

import androidx.compose.runtime.Immutable

@Immutable
data class HomeUiState(
    val totalBudget: Double = 10000.0,
    val totalSpent: Double = 0.0,
    val balance: Double = 0.0
)