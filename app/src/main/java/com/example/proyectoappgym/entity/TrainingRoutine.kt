package com.example.proyectoappgym.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrainingRoutine(
    @SerializedName("dayOfWeek")
    @Expose
    val dayOfWeek: DayOfWeek,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("exercises")
    @Expose
    val exercises: List<Exercise2>
)