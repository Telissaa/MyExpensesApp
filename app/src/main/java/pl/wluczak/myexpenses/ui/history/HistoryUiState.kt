package pl.wluczak.myexpenses.ui.history

import pl.wluczak.myexpenses.data.Expense

enum class SortOrder {
    DATE_DESC,
    DATE_ASC,
    AMOUNT_DESC,
    AMOUNT_ASC,
    CATEGORY_ASC,
    CATEGORY_DESC,
    ALPHABETICALLY_ASC,
}

sealed class FilterType {
    data object All : FilterType() // to reset filter
    data object Today : FilterType()
    data object ThisMonth : FilterType()
    data object ThisWeek : FilterType()

    data class ByCategory(val categoryNames: Set<String>) : FilterType()
    data class BySubcategory(val subcategoryNames: Set<String>) : FilterType()

    data class DateRange(val startDate: String, val endDate: String) : FilterType()
}
data class HistoryUiState(
    val expenses: List<Expense> = emptyList(),
    val availableCategories: List<String> = emptyList(), // Needed for the UI to display checkboxes
    val availableSubcategories: List<String> = emptyList(), // Needed for the UI
    val categoryToSubcategoriesMap: Map<String, List<String>> = emptyMap(), // To show correct subcategories
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentSortOrder: SortOrder = SortOrder.DATE_DESC,
    val currentFilter: FilterType = FilterType.All
)