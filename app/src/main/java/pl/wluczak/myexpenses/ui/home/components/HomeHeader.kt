package pl.wluczak.myexpenses.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
) {
    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("pl-PL"))
    val formattedDate = LocalDate.now().format(formatter)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Dziś jest $formattedDate",
            fontSize = 26.sp,
        )

        IconButton(
            onClick = {
                // logika zmiany zdjęcia / otwierania profilu
            },
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Zdjęcie profilowe",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}