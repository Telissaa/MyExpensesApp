package pl.wluczak.myexpenses.ui.history.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.wluczak.myexpenses.R
import pl.wluczak.myexpenses.ui.history.SortOrder
import pl.wluczak.myexpenses.ui.theme.darkerBlue

@Composable
fun SortMenu(
    // Przekazujemy fukcję wyższego rzędu (callback) do ViewModelu,
    // dzięki temu nasz komponent jest "głupi" i nie zna ViewModelu bezpośrednio!
    onSortSelected: (SortOrder) -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showSortMenu = true }) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = stringResource(R.string.content_description_sort),
                modifier = Modifier.size(31.dp),
                tint = darkerBlue
            )
        }

        DropdownMenu(
            expanded = showSortMenu,
            onDismissRequest = { showSortMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Najnowsze") },
                onClick = {
                    onSortSelected(SortOrder.DATE_DESC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Najstarsze") },
                onClick = {
                    onSortSelected(SortOrder.DATE_ASC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Najstarsze") },
                onClick = {
                    onSortSelected(SortOrder.AMOUNT_DESC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Najtańsze") },
                onClick = {
                    onSortSelected(SortOrder.AMOUNT_ASC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Najdroższe") },
                onClick = {
                    onSortSelected(SortOrder.AMOUNT_DESC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Kategoria A-Z") },
                onClick = {
                    onSortSelected(SortOrder.CATEGORY_ASC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Kategoria Z-A") },
                onClick = {
                    onSortSelected(SortOrder.CATEGORY_DESC)
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("A-Z") },
                onClick = {
                    onSortSelected(SortOrder.ALFABETICALLY_ASC)
                    showSortMenu = false
                }
            )
        }
    }
}