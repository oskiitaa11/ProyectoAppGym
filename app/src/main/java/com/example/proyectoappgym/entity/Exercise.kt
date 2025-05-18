package com.example.proyectoappgym.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Exercise(
    val name: String,
    val description: String,
    val exerciseLevel: ExerciseLevel,
    val type: TypeExercise,
    val trainedMuscles: List<String>,
    val series: Int,
    val repetitions: Int,
    val restBetweenSeries: Int,
    val stepsForDoIt: String,
    val typeTensExercise: TypeTensExercise? = null,
): Parcelable {
    constructor(): this("", "", ExerciseLevel.BEGINNER, TypeExercise.BASIC, emptyList<String>(), 0, 0, 0, "")
}