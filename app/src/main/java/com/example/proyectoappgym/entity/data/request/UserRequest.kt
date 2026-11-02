package com.example.proyectoappgym.entity.data.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val email: String,
    val password: String,
    @SerializedName("user_metadata")
    val userRequest: DataUserRequest
)