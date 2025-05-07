package com.example.proyectoappgym.db.retrofit.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("text")
    @Expose
    private val text: String
)