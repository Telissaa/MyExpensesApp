package pl.wluczak.myexpenses.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import pl.wluczak.myexpenses.data.Expense
import pl.wluczak.myexpenses.ui.theme.darkerBlue
import pl.wluczak.myexpenses.ui.theme.purple
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import pl.wluczak.myexpenses.R
import pl.wluczak.myexpenses.ui.history.components.FilterBottomSheet
import pl.wluczak.myexpenses.ui.history.components.SortMenu
import pl.wluczak.myexpenses.utils.formatAmount
import pl.wluczak.myexpenses.utils.formatDateString

// Helper to resolve color based on category name.
// For now, it deterministically picks a pastel color based on the category string's hashcode.
fun getCategoryColor(category: String): Color {
    val pastelColors = listOf(
        Color(0xFFFFD1DC), // Pastel Pink (like on mockup)
        Color(0xFFD1F2EB), // Pastel Teal (like on mockup)
        Color(0xFFE8F8F5), // Light Mint
        Color(0xFFE8DAEF), // Pastel Purple
        Color(0xFFFCF3CF), // Pastel Yellow
        Color(0xFFD6EAF8), // Pastel Blue
        Color(0xFFEAEDED)  // Light Gray
    )
    return if (category.isEmpty()) {
        Color(0xFFE0E0E0) // Default grey
    } else {
        val index = Math.abs(category.hashCode()) % pastelColors.size
        pastelColors[index]
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    onNavigateToAddExpense: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterSheet by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshDateFilters()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    
    Scaffold(
        topBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDFBF7)) // Slightly off-white background based on mockup
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.history_screen_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Bind FilterBottomSheet instead of a simple dropdown menu
                IconButton(onClick = { showFilterSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterList, // Zakładam, że ten import musimy dodać (Alt+Enter)
                        contentDescription = stringResource(R.string.content_description_filter),
                        modifier = Modifier.size(33.dp),
                        tint = purple
                    )
                }

                if (showFilterSheet) {
                    FilterBottomSheet(
                        availableCategories = state.availableCategories,
                        categoryToSubcategoriesMap = state.categoryToSubcategoriesMap,
                        currentFilter = state.currentFilter,
                        onDismissRequest = { showFilterSheet = false },
                        onFilterApplied = { newFilter ->
                            viewModel.onFilterChanged(newFilter)
                        }
                    )
                }

                SortMenu(
                    onSortSelected = { selectedOrder ->
                        viewModel.onSortOrderChanged(selectedOrder)
                    }
                )

            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Group expenses by date
                val groupedExpenses = state.expenses.groupBy { it.date }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    groupedExpenses.forEach { (date, expensesForDate) ->
                        stickyHeader(key = date) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFDFBF7))
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = formatDateString(date),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                                HorizontalDivider(
                                    color = Color.Black,
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth(0.3f)
                                )
                            }
                        }

                        items(
                            items = expensesForDate,
                            key = { expense -> expense.id }
                        ) { expense ->
                            HistoryExpenseItem(
                                expense = expense,
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .animateItem(),
                                onDeleteClick = viewModel::softDeleteExpense,
                                onRestoreClick = viewModel::restoreExpense,
                                onEditClick = { clickedExpense ->
                                    onNavigateToAddExpense(clickedExpense.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryExpenseItem(
    expense: Expense,
    modifier: Modifier = Modifier,
    onDeleteClick: (Expense) -> Unit = {},
    onEditClick: (Expense) -> Unit = {},
    onRestoreClick: (Expense) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // Toggles the expanded state on click
            .animateContentSize( // Automatically animates size changes (smooth expansion/collapse)
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        shape = RoundedCornerShape(16.dp),
        color = getCategoryColor(expense.category)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left side: Name and Price
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = expense.name,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatAmount(expense.amount), // Format from utils
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                
                // Right side: Category and Subcategory
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = expense.category,
                        fontSize = 14.sp,
                    )
                    if (expense.subcategory.isNotEmpty()) {
                        Text(
                            text = expense.subcategory,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
            
            // Expanded content section
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.Black.copy(alpha = 0.1f)) // Subtle separator
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side of expanded area: Photo info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!expense.productPhotoUrl.isNullOrEmpty() || !expense.receiptPhotoUrl.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = stringResource(R.string.content_description_photo_attached),
                                modifier = Modifier.size(20.dp),
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.expense_item_photo_label),
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.expense_no_photos),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Right side of expanded area: Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (expense.deletedAt != null) {
                            // Item is softly deleted, show restore action
                            IconButton(onClick = { onRestoreClick(expense) }) {
                                Icon(
                                    imageVector = Icons.Default.Restore,
                                    contentDescription = stringResource(R.string.action_restore_expense),
                                    tint = Color(0xFF4CAF50) // Green
                                )
                            }
                        } else {
                            // Item is active, show delete and edit actions
                            IconButton(onClick = { onDeleteClick(expense) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.action_delete_expense),
                                    tint = Color.Red.copy(alpha = 0.8f)
                                )
                            }
                            IconButton(onClick = { onEditClick(expense) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.action_edit_expense),
                                    tint = darkerBlue
                                )
                            }
                        }
                    }
                }
            } else {
                // Not expanded: Show minimal photo indicator if photos exist
                if (!expense.productPhotoUrl.isNullOrEmpty() || !expense.receiptPhotoUrl.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = stringResource(R.string.content_description_photo_attached),
                            modifier = Modifier.size(16.dp),
                            tint = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.expense_item_photo_label),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}