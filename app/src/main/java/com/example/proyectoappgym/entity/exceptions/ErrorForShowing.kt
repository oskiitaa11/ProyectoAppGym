package com.example.proyectoappgym.entity.exceptions

data class ErrorForShowing(
    val error: String,
    val idIcon: Int?,
    val errorType: ErrorType
)