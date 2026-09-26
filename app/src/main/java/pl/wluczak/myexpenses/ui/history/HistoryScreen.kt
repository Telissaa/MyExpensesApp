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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Divider

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

// Helper to format date if it comes as "yyyy-MM-dd" to "dd.MM.yyyy" for the header
fun formatDateString(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (_: DateTimeParseException) {
        dateString // return original if it cannot be parsed
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
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
                text = "Historia",
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
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrowanie",
                    modifier = Modifier.size(33.dp),
                    tint = purple
                )
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Sortowanie",
                    modifier = Modifier.size(31.dp),
                    tint = darkerBlue
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
                        stickyHeader {
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
                                Divider(
                                    color = Color.Black,
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth(0.3f)
                                )
                            }
                        }

                        items(expensesForDate) { expense ->
                            HistoryExpenseItem(
                                expense = expense,
                                modifier = Modifier.padding(bottom = 12.dp)
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
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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
                        text = "${expense.amount}", // Format as needed, e.g. "%.2f zł"
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Bottom section: Photo icon (optional)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!expense.productPhotoUrl.isNullOrEmpty() || !expense.receiptPhotoUrl.isNullOrEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Zdjecie dodane",
                        modifier = Modifier.size(16.dp),
                        tint = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "zdjęcie",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                } else {
                     // Empty space if no icon to maintain layout structure if needed,
                     // but based on mockup, it just says "zdjęcie ewentualnie", so we show icon + text if present.
                }
            }
        }
    }
}