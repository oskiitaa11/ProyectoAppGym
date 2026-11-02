package com.example.proyectoappgym.entity.data.request

import com.example.proyectoappgym.entity.trainingroutines.DayOfWeek
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrainingRoutineRequest(
    @SerializedName("day_of_week")
    @Expose
    val dayOfWeek: DayOfWeek?,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("realization_exercises")
    @Expose
    val exercises: List<RealizationExerciseRequest>
)  {
    companion object {
        fun getTrainingRoutineRequestFromRoutine(trainingRoutine: TrainingRoutine): TrainingRoutineRequest {
            val realizationExerciseRequest = trainingRoutine.exercises.map { RealizationExerciseRequest.getRealizationExercisesRequest(it) }

            return TrainingRoutineRequest(trainingRoutine.dayOfWeek, trainingRoutine.name, realizationExerciseRequest)
        }
    }
}