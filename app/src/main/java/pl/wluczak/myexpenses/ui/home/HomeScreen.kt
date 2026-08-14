package pl.wluczak.myexpenses.ui.home

import pl.wluczak.myexpenses.ui.home.components.HomeHeader
import pl.wluczak.myexpenses.ui.home.components.SummaryCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.wluczak.myexpenses.ui.addexpense.ExpenseViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseViewModel = viewModel(),
) {
    // data
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
    ){
        HomeHeader()
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SummaryCard(
                title = "Budżet",
                amount = "${state.totalBudget} zł",
                modifier = Modifier.weight(1f),
            )
            SummaryCard(
                title = "Wydatki",
                amount = "${state.totalSpent} zł",
                modifier = Modifier.weight(1f),
            )
        }
    }
}