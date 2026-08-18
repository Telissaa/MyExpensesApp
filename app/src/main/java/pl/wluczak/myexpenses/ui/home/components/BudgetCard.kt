package pl.wluczak.myexpenses.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BudgetCard(
    budget: String,
    spent: String,
    iconSize: Dp = 60.dp,
    modifier: Modifier = Modifier,
) {
    // Outbox
    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(210.dp)
            .background(MaterialTheme.colorScheme.secondary),
    ) {
        // First inner box
        Box(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.TopStart)
                .fillMaxWidth(0.55f)
                .fillMaxHeight(0.50f)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = budget,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.offset(y = 10.dp),
                )
                Text(
                    text = "Budżet",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.offset(y = 10.dp),
                )
            }
        }

        // Second inner box
        Box(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.60f)
                .fillMaxHeight(0.60f)
                .background(MaterialTheme.colorScheme.tertiary),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = spent,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.offset(y = 10.dp),
                )
                Text(
                    text = "zostało",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.offset(y = 12.dp),
                )
            }
        }
        IconButton(
            onClick = {
                // przenosi do dodawania wydatku
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp, bottom = 130.dp)
                .size(iconSize),
        ) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "Plus",
                modifier = Modifier.fillMaxSize(),
                tint = Color(0xFF00b9f0),
            )
        }
    }
}
