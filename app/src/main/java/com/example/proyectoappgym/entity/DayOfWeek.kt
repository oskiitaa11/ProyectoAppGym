package com.example.proyectoappgym.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class DayOfWeek(val stringValue: String, val idDay: Int) {
    MONDAY("Monday", 1), TUESDAY("Tuesday", 2), WEDNESDAY("Wednesday", 3), THURSDAY("Thursday", 4), FRIDAY("Friday", 5),
    SATURDAY("Saturday", 6), SUNDAY("Sunday", 7);

    companion object {
        fun fromString(dayOfWeek: String): DayOfWeek {
            return entries.find { it.stringValue.lowercase() == dayOfWeek.lowercase() } ?: MONDAY
        }
    }
}