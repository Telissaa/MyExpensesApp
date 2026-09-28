package pl.wluczak.myexpenses.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.wluczak.myexpenses.data.Expense
import pl.wluczak.myexpenses.data.ExpenseRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.flatMapLatest

class HistoryViewModel(private val repository: ExpenseRepository): ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    private val _sortOrder = MutableStateFlow(SortOrder.DATE_DESC)
    private val _filterType = MutableStateFlow<FilterType>(FilterType.All)

    fun onSortOrderChanged(newSortOrder: SortOrder) {
        _sortOrder.value = newSortOrder
    }
    fun onFilterChanged(newFilter: FilterType) {
        _filterType.value = newFilter
    }

    init{
        // Loading hard delete for every 2 days
        viewModelScope.launch {
            val twoDaysInMillis = 2L * 24 * 60 * 60 * 1000
            val thresholdTime = System.currentTimeMillis() - twoDaysInMillis
            repository.cleanupOldDeletedExpenses(thresholdTime)
        }
        @OptIn(ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            // React to filter changes and prevent race conditions by putting combine inside flatMapLatest
            _filterType.flatMapLatest { filterType ->
                val sourceFlow = if (filterType is FilterType.Deleted) {
                    repository.getDeletedExpenses()
                } else {
                    repository.getAllExpenses()
                }

                combine(sourceFlow, _sortOrder) { expensesList, sortOrder ->
                    val filteredList = when (filterType) {
                        FilterType.All -> expensesList

                    FilterType.Today -> {
                        val today = LocalDate.now()
                        val todayString = today.toString() // Default format of LocalDate is yyyy-MM-dd
                        expensesList.filter { expense ->
                            expense.date == todayString
                        }
                    }

                    FilterType.ThisMonth -> {
                        val today = java.time.LocalDate.now()
                        val currentMonth = today.monthValue
                        val currentYear = today.year
                        expensesList.filter { expense ->
                            try{
                            val expenseDate = java.time.LocalDate.parse(expense.date)
                            expenseDate.monthValue == currentMonth && expenseDate.year == currentYear
                            }catch (_: Exception) {
                                false
                            }
                        }
                    }
                    FilterType.ThisWeek -> {
                        val today = java.time.LocalDate.now()
                        val currentYear = today.year
                        val currentWeek = today.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear())
                        expensesList.filter { expense ->
                            try{
                                val expenseDate = java.time.LocalDate.parse(expense.date)
                                expenseDate.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear()) == currentWeek && expenseDate.year == currentYear
                            }catch (_: Exception) {
                                false
                            }
                        }
                    }
                    is FilterType.ByCategory -> {
                        expensesList.filter { expense ->
                            filterType.categoryNames.contains(expense.category)
                        }
                    }
                    is FilterType.BySubcategory -> {
                        expensesList.filter { expense ->
                            filterType.subcategoryNames.contains(expense.subcategory)
                        }
                    }
                    else -> expensesList
                }

            val uniqueCategories = expensesList.map { it.category }.filter { it.isNotEmpty() }.distinct().sorted()

            // Extract categories with their specific subcategories into a Map to feed the UI
            val categoryToSubcategoriesMap = expensesList
                .filter { it.category.isNotEmpty() && it.subcategory.isNotEmpty() }
                .groupBy({ it.category }, { it.subcategory })
                .mapValues { it.value.distinct().sorted() }

            val sortedList = when(sortOrder) {
                SortOrder.DATE_DESC -> filteredList.sortedByDescending { it.date }
                SortOrder.DATE_ASC -> filteredList.sortedBy { it.date }
                SortOrder.AMOUNT_DESC -> filteredList.sortedByDescending { it.amount }
                SortOrder.AMOUNT_ASC -> filteredList.sortedBy { it.amount }
                SortOrder.CATEGORY_ASC -> filteredList.sortedBy { it.category }
                SortOrder.CATEGORY_DESC -> filteredList.sortedByDescending { it.category }
                SortOrder.ALPHABETICALLY_ASC -> filteredList.sortedBy { it.name }
            }
            // Return our own data class with the results because Triple only holds 3 items!
            FilterResult(sortedList, sortOrder, filterType, uniqueCategories, emptyList(), categoryToSubcategoriesMap)
        }
        }.collect { result ->
            _uiState.value = _uiState.value.copy(
                expenses = result.list,
                availableCategories = result.categories,
                availableSubcategories = result.subcategories,
                categoryToSubcategoriesMap = result.categoryToSubcategoriesMap,
                currentSortOrder = result.sortOrder,
                currentFilter = result.filterType,
                isLoading = false
                )
            }
        }
    }

    fun softDeleteExpense(expense: Expense) {
        viewModelScope.launch {
            val currentTimeMillis = System.currentTimeMillis()
            val softDeletedExpense = expense.copy(deletedAt = currentTimeMillis)
            repository.updateExpense(softDeletedExpense)
        }
    }
    
    fun restoreExpense(expense: Expense) {
        viewModelScope.launch {
            val restoredExpense = expense.copy(deletedAt = null)
            repository.updateExpense(restoredExpense)
        }
    }
}

// Private helper class to pack the results from the combine block
private data class FilterResult(
    val list: List<Expense>,
    val sortOrder: SortOrder,
    val filterType: FilterType,
    val categories: List<String>,
    val subcategories: List<String>,
    val categoryToSubcategoriesMap: Map<String, List<String>> = emptyMap()
)