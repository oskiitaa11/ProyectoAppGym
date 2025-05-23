package com.example.proyectoappgym.db.retrofit.entity

import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.RealizationExercise
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrainingRoutineName(
    @SerializedName("dayOfWeek")
    @Expose
    val dayOfWeek: DayOfWeek,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("exercises")
    @Expose
    val exercises: List<String>
)