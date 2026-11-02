package com.example.proyectoappgym.entity.exceptions

import android.R
import kotlinx.serialization.json.Json

class ErrorException(val error: String, override val message: String): Exception(message) {
    companion object {
        fun fromJson(errorResponse: String, errorResponseAdditional: ErrorException): ErrorException {
            val json = Json { ignoreUnknownKeys = true }
            val errorResponseBackend: ErrorResponseBackend
            var errorException: ErrorException

            try {
                errorResponseBackend = json.decodeFromString<ErrorResponseBackend>(errorResponse)
                errorException = ErrorException(errorResponseBackend.error, errorResponseBackend.message)
            } catch (e: Exception) {
                errorException = errorResponseAdditional
            }

            return errorException

        }
    }
}