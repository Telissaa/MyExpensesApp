package pl.wluczak.myexpenses.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun formatDateString(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (_: DateTimeParseException) {
        dateString // return original if it cannot be parsed
    }
}