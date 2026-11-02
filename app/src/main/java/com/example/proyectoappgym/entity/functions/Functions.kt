package com.example.proyectoappgym.entity.functions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getParsedBirthdate(birthDate: String): String {
    val birthDateWithoutBar = birthDate.replace("/", "-")
    val inputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val parsedBirthDate: String

    if (birthDate.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) return birthDate

    parsedBirthDate = LocalDate.parse(birthDateWithoutBar, inputFormat).toString()

    return parsedBirthDate.format(outputFormat)
}

fun getErrorForShowingScreen(error: String, message: String): ErrorForShowing {
    return when(error) {
        "user_not_found" -> ErrorForShowing(message, R.drawable.ic_user_not_found, ErrorType.ERROR_IN_ERROR_SCREEN)
        "routines_not_found" -> ErrorForShowing(message, R.drawable.ic_error_routine_not_found, ErrorType.ERROR_IN_ERROR_SCREEN)
        "invalid_credentials" -> ErrorForShowing(message, R.drawable.ic_invalid_credentials, ErrorType.ERROR_IN_SAME_SCREEN)
        else -> ErrorForShowing(message, R.drawable.ic_error, ErrorType.ERROR_IN_SAME_SCREEN )
    }
}