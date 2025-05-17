package com.example.proyectoappgym.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Exercise(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("exerciseLevel")
    @Expose
    val exerciseLevel: ExerciseLevel,
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
): Parcelable {
    constructor(): this("", "", ExerciseLevel.BEGINNER, TypeExercise.BASIC, emptyList<String>(), 0, 0, 0)
}