package pl.wluczak.myexpenses.utils

import java.text.NumberFormat
import java.util.Locale

fun formatAmount(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("pl", "PL"))
    formatter.minimumFractionDigits = 0
    formatter.maximumFractionDigits = 2

    return formatter.format(amount)
}