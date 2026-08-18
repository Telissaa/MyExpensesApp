package pl.wluczak.myexpenses.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
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
fun ExpenseCard(
    text: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 60.dp,
) {

    Box(//outbox
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(210.dp)
            .background(MaterialTheme.colorScheme.primary)

    ) {
        Text(
            text = "Twoje wydatki:",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(15.dp),
        )

        Box(//inner box
            modifier = Modifier
                .size(230.dp, 90.dp)
                .align(Alignment.Center)
                .offset(y = 20.dp)
                .background(Color.White)
        ) {
            Text(
                text = text,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center),
            )
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
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = "Plus",
                modifier = Modifier.fillMaxSize(),
                tint = Color(0xFF00b9f0),
            )
        }
    }
}