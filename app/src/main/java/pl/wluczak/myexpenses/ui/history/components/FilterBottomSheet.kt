package pl.wluczak.myexpenses.ui.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.wluczak.myexpenses.ui.history.FilterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    availableCategories: List<String>,
    categoryToSubcategoriesMap: Map<String, List<String>>,
    currentFilter: FilterType,
    onDismissRequest: () -> Unit,
    onFilterApplied: (FilterType) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(currentFilter) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Filtrowanie",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter is FilterType.Today,
                    onClick = { 
                        selectedFilter = if (selectedFilter is FilterType.Today) FilterType.All else FilterType.Today 
                    },
                    label = { Text("Dzisiaj") }
                )
                FilterChip(
                    selected = selectedFilter is FilterType.ThisWeek,
                    onClick = { 
                        selectedFilter = if (selectedFilter is FilterType.ThisWeek) FilterType.All else FilterType.ThisWeek 
                    },
                    label = { Text("Ten tydzień") }
                )
                FilterChip(
                    selected = selectedFilter is FilterType.ThisMonth,
                    onClick = { 
                        selectedFilter = if (selectedFilter is FilterType.ThisMonth) FilterType.All else FilterType.ThisMonth 
                    },
                    label = { Text("Ten miesiąc") }
                )
                FilterChip(
                    selected = selectedFilter is FilterType.Deleted,
                    onClick = { 
                        selectedFilter = if (selectedFilter is FilterType.Deleted) FilterType.All else FilterType.Deleted 
                    },
                    label = { Text("Usunięte") }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Kategorie",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val currentlySelectedCategories = (selectedFilter as? FilterType.ByCategory)?.categoryNames ?: emptySet()

                availableCategories.forEach { categoryName ->
                    val isSelected = currentlySelectedCategories.contains(categoryName)

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) {
                                currentlySelectedCategories - categoryName // Remove from set
                            } else {
                                currentlySelectedCategories + categoryName // Add to set
                            }

                            selectedFilter = if (newSet.isEmpty()) {
                                FilterType.All
                            } else {
                                FilterType.ByCategory(newSet)
                            }
                        },
                        label = { Text(categoryName) }
                    )
                }
            }
            
            val currentlySelectedCategories = (selectedFilter as? FilterType.ByCategory)?.categoryNames ?: emptySet()
            
            var lastSelectedCategories by remember { mutableStateOf<Set<String>>(emptySet()) }
            
            LaunchedEffect(selectedFilter) {
                if (selectedFilter is FilterType.ByCategory && currentlySelectedCategories.isNotEmpty()) {
                    lastSelectedCategories = currentlySelectedCategories
                } else if (selectedFilter is FilterType.All || selectedFilter is FilterType.Today || selectedFilter is FilterType.ThisMonth || selectedFilter is FilterType.ThisWeek) {
                    lastSelectedCategories = emptySet()
                }
            }
            
            val shouldShowSubcategories = currentlySelectedCategories.isNotEmpty() || (selectedFilter is FilterType.BySubcategory)
            
            if (shouldShowSubcategories) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Podkategorie",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val currentlySelectedSubcategories = (selectedFilter as? FilterType.BySubcategory)?.subcategoryNames ?: emptySet()

                    val relevantSubcategories = lastSelectedCategories.flatMap { category ->
                        categoryToSubcategoriesMap[category] ?: emptyList()
                    }.distinct().sorted()

                    relevantSubcategories.forEach { subcategoryName ->
                        val isSelected = currentlySelectedSubcategories.contains(subcategoryName)

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val newSet = if (isSelected) {
                                    currentlySelectedSubcategories - subcategoryName
                                } else {
                                    currentlySelectedSubcategories + subcategoryName
                                }

                                selectedFilter = if (newSet.isEmpty()) {
                                    // If all subcategories are deselected, revert to category filter
                                    if(lastSelectedCategories.isNotEmpty()) {
                                        FilterType.ByCategory(lastSelectedCategories)
                                    } else {
                                        FilterType.All
                                    }
                                } else {
                                    FilterType.BySubcategory(newSet)
                                }
                            },
                            label = { Text(subcategoryName) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================

            // Dolne Przyciski Akcji
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { 
                    selectedFilter = FilterType.All // Reset filter
                }) {
                    Text("Wyczyść")
                }
                
                Button(onClick = {
                    onFilterApplied(selectedFilter) // Apply filter
                    onDismissRequest()              // Close bottom sheet
                }) {
                    Text("Zastosuj")
                }
            }
        }
    }
}
