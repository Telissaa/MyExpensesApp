package pl.wluczak.myexpenses.ui.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.wluczak.myexpenses.data.Expense
import pl.wluczak.myexpenses.data.ExpenseDao
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddExpenseViewModel(
    private val expenseDao: ExpenseDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    private val defaultCategories = listOf("Jedzenie", "Transport", "Rozrywka", "Rachunki", "Zakupy")
    private var subcategoriesJob: Job? = null

    init {
        // Ładujemy unikalne kategorie z bazy w tle (Dispatchers.IO) bez blokowania wątku głównego
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.getAllCategories().collect { dbCategories ->
                val combined = (defaultCategories + dbCategories).distinct()
                _uiState.update { it.copy(availableCategories = combined) }
            }
        }
    }

    private fun loadSubcategories(categoryName: String) {
        subcategoriesJob?.cancel()
        if (categoryName.isBlank()) {
            _uiState.update { it.copy(availableSubCategories = emptyList()) }
            return
        }

        subcategoriesJob = viewModelScope.launch(Dispatchers.IO) {
            expenseDao.getSubcategoriesForCategory(categoryName).collect { dbSubcategories ->
                _uiState.update { it.copy(availableSubCategories = dbSubcategories) }
            }
        }
    }

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName, errorMessage = null) }
    }

    fun onAmountChanged(newAmount: String) {
        _uiState.update { it.copy(amount = newAmount, errorMessage = null) }
    }

    fun onDateChanged(newDate: String) {
        _uiState.update { it.copy(date = newDate, errorMessage = null) }
    }

    fun onCategoryChanged(newCategory: String) {
        _uiState.update {
            it.copy(
                category = newCategory,
                subCategory = "", // Czyszczenie podkategorii przy zmianie głównej kategorii
                errorMessage = null
            )
        }
        loadSubcategories(newCategory)
    }

    fun onCategorySelected(selectedCategory: String) {
        _uiState.update {
            it.copy(
                category = selectedCategory,
                subCategory = "", // Czyszczenie podkategorii przy zmianie głównej kategorii
                isAddingCustomCategory = false,
                isAddingCustomSubCategory = false,
                errorMessage = null
            )
        }
        loadSubcategories(selectedCategory)
    }

    fun onCustomCategoryToggle(showInput: Boolean) {
        _uiState.update {
            it.copy(
                isAddingCustomCategory = showInput,
                errorMessage = null
            )
        }
    }

    fun onSubCategorySelected(selectedSubCategory: String) {
        _uiState.update {
            it.copy(
                subCategory = selectedSubCategory,
                isAddingCustomSubCategory = false,
                errorMessage = null
            )
        }
    }

    fun onCustomSubCategoryToggle(showInput: Boolean) {
        _uiState.update {
            it.copy(
                isAddingCustomSubCategory = showInput,
                errorMessage = null
            )
        }
    }

    fun onSubCategoryChanged(newSubCategory: String) {
        _uiState.update { it.copy(subCategory = newSubCategory, errorMessage = null) }
    }

    fun onProductImageUriChanged(newUri: String?) {
        _uiState.update { it.copy(productImageUri = newUri, errorMessage = null) }
    }

    fun onReceiptImageUriChanged(newUri: String?) {
        _uiState.update { it.copy(receiptImageUri = newUri, errorMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun saveExpense(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                // Walidacja danych przed zapisem
                if (currentState.name.isBlank()) {
                    throw IllegalArgumentException("Nazwa wydatku nie może być pusta.")
                }

                val amountValue = currentState.amount.replace(',', '.').toDoubleOrNull()
                if (amountValue == null || amountValue <= 0.0) {
                    throw IllegalArgumentException("Podaj poprawną kwotę większą od zera.")
                }

                val formattedDate = try {
                    if (currentState.date.isNotBlank()) {
                        val parsed = LocalDate.parse(currentState.date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        parsed.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    } else {
                        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }
                } catch (e: Exception) {
                    currentState.date.ifBlank { LocalDate.now().toString() }
                }

                val expense = Expense(
                    name = currentState.name.trim(),
                    amount = amountValue,
                    date = formattedDate,
                    category = currentState.category.ifBlank { "Inne" },
                    subcategory = currentState.subCategory.trim()
                )

                // Próba zapisu w bazie danych
                expenseDao.insertExpense(expense)

                _uiState.update { it.copy(isLoading = false) }

                // Pomyślny zapis - powiadamiamy UI
                onSuccess()
            } catch (e: IllegalArgumentException) {
                // Błąd walidacji - ustawiamy komunikat o błędzie, zachowując wszystkie wprowadzone dane
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            } catch (e: Exception) {
                // Błąd bazy danych / nieoczekiwany - zachowujemy wprowadzone dane
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Błąd zapisu w bazie: ${e.localizedMessage ?: "Spróbuj ponownie"}"
                    )
                }
            }
        }
    }
}
