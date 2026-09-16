package com.example.proyectoappgym.entity

import com.example.proyectoappgym.entity.ExercisesName
import kotlinx.serialization.Serializable

@Serializable
data class RealizationExercise(
    private var _exercise: Exercise,
    private var _series: Int,
    val repetitions: Int,
    val restBetweenSeries: Int
) {
    constructor(): this(ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE.exercise, 0, 0, 0)

    val exercise: Exercise
        get() = _exercise
    val series: Int
        get() = _series

    fun changeExercise(exercise: Exercise) {
        _exercise = exercise
    }

    fun changeSets(newSets: Int) {
        _series = newSets
    }
}