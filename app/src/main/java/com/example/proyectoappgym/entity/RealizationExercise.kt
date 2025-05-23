package com.example.proyectoappgym.entity

import com.example.proyectoappgym.db.retrofit.entity.ExercisesName
import kotlinx.serialization.Serializable

@Serializable
data class RealizationExercise(
    val exercise: ExercisesName,
    val series: Int,
    val repetitions: Int,
    val restBetweenSeries: Int
) {
    constructor(): this(ExercisesName.BARBELL_CURL, 0, 0, 0)
}