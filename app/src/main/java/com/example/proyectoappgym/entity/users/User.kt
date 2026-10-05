package com.example.proyectoappgym.entity.users

import com.example.proyectoappgym.entity.users.Gender
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    val username: String,
    var password: String,
    val email: String,
    var name: String,
    val birthdate: String,
    val gender: Gender,
    var allQuestionsAnswered: Map<String, List<String>> = mapOf(),
    @SerializedName("profileAvatar")
    @Expose
    var idProfileAvatar: Int = 1,
    private var _trainingRoutines: List<TrainingRoutine> = emptyList<TrainingRoutine>()
) {
    constructor(): this("", "", "", "", "", Gender.NONE)

    val trainingRoutines: List<TrainingRoutine>
        get() = _trainingRoutines

    fun addTrainingRoutines(newTrainingRoutines: List<TrainingRoutine>) {
        _trainingRoutines = newTrainingRoutines
    }
}