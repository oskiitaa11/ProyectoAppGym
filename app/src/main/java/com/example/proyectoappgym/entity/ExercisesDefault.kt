package com.example.proyectoappgym.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExercisesDefault(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("exercises")
    @Expose
    var exercises: List<ExercisesName>
)