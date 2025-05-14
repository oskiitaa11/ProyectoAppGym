package com.example.proyectoappgym.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class DayOfWeek(val stringValue: String) {
    MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday"), SATURDAY(
        "Saturday"
    ), SUNDAY("Sunday");

    companion object {
        fun fromString(dayOfWeek: String): DayOfWeek {
            return entries.find { it.stringValue.lowercase() == dayOfWeek.lowercase() } ?: MONDAY
        }
    }
}