package com.example.proyectoappgym.entity.trainingroutines

import com.example.proyectoappgym.entity.data.request.TrainingRoutineRequest
import com.example.proyectoappgym.entity.trainingroutines.DayOfWeek
import com.example.proyectoappgym.entity.realizationexercises.RealizationExercise
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TrainingRoutine(
    @SerializedName("dayOfWeek")
    @Expose
    val dayOfWeek: DayOfWeek?,
    @SerializedName("name")
    @Expose
    private var _name: String,
    @SerializedName("exercises")
    @Expose
    private var _exercises: List<RealizationExercise>
) {
    constructor(): this(DayOfWeek.MONDAY, "", emptyList())

    val exercises: List<RealizationExercise>
        get() = _exercises

    val name: String
        get() = _name

    fun addExercises(exercisesToAdd: List<RealizationExercise>) {
        val newExercises = exercises.toMutableList()

        newExercises.addAll(exercisesToAdd)
        _exercises = newExercises
    }

    fun changeName(newName: String) {
        _name = newName
    }

    companion object {
        fun getTrainingRoutineFromResponse(trainingRoutine: TrainingRoutineRequest): TrainingRoutine {
            val realizationExercises = trainingRoutine.exercises.map { RealizationExercise.getRealizationExerciseFromResponse(it) }

            return TrainingRoutine(trainingRoutine.dayOfWeek, trainingRoutine.name, realizationExercises)
        }
    }
}