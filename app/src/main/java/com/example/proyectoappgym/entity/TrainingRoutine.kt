package com.example.proyectoappgym.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
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
        //var realizationExerciseExample = _exercises.find { it.exercise.type == typeExercise } ?: return
        var newExercises = exercises.toMutableList()
        //var realizationExerciseToAdd = exercisesToAdd.map { RealizationExercise(it, realizationExerciseExample.series, realizationExerciseExample.repetitions, realizationExerciseExample.restBetweenSeries) }

        newExercises.addAll(exercisesToAdd)
        _exercises = newExercises
    }

    fun changeName(newName: String) {
        _name = newName
    }
}