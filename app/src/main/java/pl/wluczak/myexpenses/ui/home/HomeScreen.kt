package pl.wluczak.myexpenses.ui.home

import pl.wluczak.myexpenses.ui.home.components.HomeHeader
import pl.wluczak.myexpenses.ui.home.components.ExpenseCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import pl.wluczak.myexpenses.ui.addexpense.ExpenseViewModel
import pl.wluczak.myexpenses.ui.home.components.BudgetCard
import pl.wluczak.myexpenses.utils.formatAmount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseViewModel = viewModel(),
) {
    // data
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 25.dp, vertical = 5.dp),
    ) {
        HomeHeader()
        Spacer(modifier = Modifier.height(20.dp))
        ExpenseCard(
            text = "${formatAmount(state.totalBudget)} zł",
        )
        Spacer(modifier = Modifier.height(20.dp))
        BudgetCard(
            budget = "${formatAmount(state.totalBudget)} zł",
            spent = "${formatAmount(state.balance)} zł",
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // TODO: Logika zostanie dodana później
            },
            modifier = Modifier
                .size(250.dp, 60.dp)
                .align(Alignment.End),
            shape = RoundedCornerShape(0.dp),// Ostre krawędzie jak w Boxie
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDCDCDC),
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Zaprojektuj swój budżet",
                fontSize = 18.sp,
            )
        }
    }
}