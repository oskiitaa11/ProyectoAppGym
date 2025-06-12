package com.example.proyectoappgym.entity

import com.example.proyectoappgym.entity.ExercisesName
import kotlinx.serialization.Serializable

@Serializable
data class RealizationExercise(
    val exercise: ExercisesName,
    val series: Int,
    val repetitions: Int,
    val restBetweenSeries: Int
) {
    constructor(): this(ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE, 0, 0, 0)
}