package com.example.proyectoappgym.entity.data.request

import com.example.proyectoappgym.entity.realizationexercises.RealizationExercise
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class RealizationExerciseRequest(
    @SerializedName("id_exercise")
    val idExercise: Int,
    val sets: Int,
    @SerializedName("max_repetitions")
    val maxRepetitions: Int,
    @SerializedName("min_repetitions")
    val minRepetitions: Int,
    @SerializedName("rest_between_sets")
    val restBetweenSeries: Int
) {
    companion object {
        fun getRealizationExercisesRequest(realizationExercise: RealizationExercise): RealizationExerciseRequest {

            return RealizationExerciseRequest(realizationExercise.exercise.id,
                realizationExercise.series,
                realizationExercise.maxRepetitions,
                realizationExercise.minRepetitions,
                realizationExercise.restBetweenSeries)
        }
    }
}