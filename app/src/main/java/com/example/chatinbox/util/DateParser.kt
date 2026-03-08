package com.example.chatinbox.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun parseDate(date: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    return format.parse(date)?.time ?: 0L
}


private val messageFormatter = SimpleDateFormat(
    "MMM d, yyyy h:mm a",
    Locale.getDefault()
)

fun formatMessageDate(timestamp: Long): String {
    return messageFormatter.format(Date(timestamp))
}