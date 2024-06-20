package com.kiselev.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeParseException
import java.util.*

fun Date.toDataString(pattern: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun String.toDateTime(pattern: String, locale: Locale = Locale.getDefault()): Date? {
    return try {
        val formatter = SimpleDateFormat(pattern, locale)
        formatter.parse(this)
    } catch (e: DateTimeParseException) {
        null
    }
}
