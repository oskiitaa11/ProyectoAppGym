package com.example.proyectoappgym.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class Exercise2(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("type")
    @Expose
    val type: TypeExercise,
    @SerializedName("trainedMuscles")
    @Expose
    val trainedMuscles: List<String>,
    @SerializedName("series")
    @Expose
    val series: Int,
    @SerializedName("repetitions")
    @Expose
    val repetitions: Int,
    @SerializedName("restBetweenSeries")
    @Expose
    val restBetweenSeries: Int,
    val typeTensExercise: TypeTensExercise? = null,
)