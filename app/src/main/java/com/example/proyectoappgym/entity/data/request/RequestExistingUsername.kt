package com.example.proyectoappgym.entity.data.request

import com.google.gson.annotations.SerializedName


data class RequestExistingUsername(
    val username: String,
    @SerializedName("id_for_client")
    val idForClient: Long?
)