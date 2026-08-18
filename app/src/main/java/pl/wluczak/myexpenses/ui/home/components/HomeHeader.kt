package pl.wluczak.myexpenses.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    // logika zmiany zdjęcia i otwierania profilu
                },
                modifier = Modifier.size(45.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Zdjęcie profilowe",
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Mały odstęp pionowy między ikoną a napisem z datą
        Spacer(modifier = Modifier.height(10.dp))

        // 2. Napis z dzisiejszym dniem (automatycznie ląduje po lewej stronie w nowej linii)
        Text(
            text = "Dziś jest $formattedDate",
            fontSize = 26.sp
        )
    }
}
