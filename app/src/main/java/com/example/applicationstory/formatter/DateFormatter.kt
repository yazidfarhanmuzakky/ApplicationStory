package com.example.applicationstory.formatter

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateFormatter {
    fun formatDate(inputDate: String, targetTimeZone: String): String {
        val instant = Instant.parse(inputDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }
}