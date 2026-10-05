package com.example.proyectoappgym.entity.data

import com.google.gson.annotations.SerializedName

class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int
)