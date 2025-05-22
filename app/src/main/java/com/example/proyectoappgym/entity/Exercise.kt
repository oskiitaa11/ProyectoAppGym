package com.example.proyectoappgym.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val description: String,
    val exerciseLevel: ExerciseLevel,
    val type: TypeExercise,
    val trainedPrimaryMuscles: List<Muscles>,
    val trainedSecondaryMuscles: List<Muscles>,
    val nameVideo: String,
    val stepsForDoIt: String,
    val typeTensExercise: TypeTensExercise? = null,
) {
    constructor(): this("", "", ExerciseLevel.BEGINNER, TypeExercise.BASIC, emptyList(), emptyList(), "", "")
}