package com.example.proyectoappgym.entity.exceptions

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ErrorResponseBackend(
    @SerialName("error_code")
    private var _error: String,
    @SerialName("msg")
    private var _message: String
) {
    val error: String
        get() = _error
    val message: String
        get() = _message
}