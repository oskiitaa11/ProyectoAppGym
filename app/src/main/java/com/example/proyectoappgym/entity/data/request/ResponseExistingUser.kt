package com.example.proyectoappgym.entity.data.request

import com.google.gson.annotations.SerializedName

data class ResponseExistingUser(
    @SerializedName("exists_username")
    val existsUser: Boolean,
    @SerializedName("id_for_client")
    val idForClient: Long?
)