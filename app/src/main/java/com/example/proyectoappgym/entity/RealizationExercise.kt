package com.example.proyectoappgym.entity

import kotlinx.serialization.Serializable

@Serializable
data class RealizationExercise(
    val exercise: Exercise,
    val series: Int,
    val repetitions: Int,
    val restBetweenSeries: Int
) {
    constructor(): this(Exercise(), 0, 0, 0)
}