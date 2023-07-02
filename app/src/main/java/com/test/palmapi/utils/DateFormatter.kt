package com.test.palmapi.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTimeAgo(timeInMillis: Long): String {
    val currentTime = System.currentTimeMillis()
    val elapsedTime = currentTime - timeInMillis

    // Calculate the time difference in seconds, minutes, hours, and days
    val seconds = elapsedTime / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 3 -> formatDate(timeInMillis) // Format as a date if more than 3 days ago
        days > 0 -> "$days days ago"
        hours > 0 -> "$hours hours ago"
        minutes > 0 -> "$minutes minutes ago"
        else -> "$seconds seconds ago"
    }
}

fun formatDate(timeInMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return dateFormat.format(Date(timeInMillis))
}
