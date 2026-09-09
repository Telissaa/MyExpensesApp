package pl.wluczak.myexpenses.ui.addexpense

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AddExpenseUiState(
    val name: String = "",
    val amount: String = "",
    val date: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
    val category: String = "",
    val subCategory: String = "",
    val availableCategories: List<String> = listOf("Jedzenie", "Transport", "Rozrywka", "Rachunki", "Zakupy"),
    val availableSubCategories: List<String> = emptyList(),
    val isAddingCustomCategory: Boolean = false,
    val isAddingCustomSubCategory: Boolean = false,
    val productImageUri: String? = null,
    val receiptImageUri: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)