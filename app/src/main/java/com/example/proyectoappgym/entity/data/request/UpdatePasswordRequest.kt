package com.example.proyectoappgym.entity.data.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("current_password")
    val actualPassword: String,
    @SerializedName("password")
    val newPassword: String
)