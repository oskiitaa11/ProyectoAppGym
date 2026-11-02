package com.example.proyectoappgym.entity.users

import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    val username: String,
    val password: String,
    val email: String,
    @SerializedName("full_name")
    val name: String,
    @SerializedName("birth_date")
    val birthdate: String,
    val gender: Gender,
    @SerializedName("all_answered_questions")
    private var _allQuestionsAnswered: Map<String, List<String>> = mapOf(),
    @SerializedName("id_profile_avatar")
    @Expose
    var idProfileAvatar: Int = 1,
    @SerializedName("routines")
    private var _trainingRoutines: List<TrainingRoutine> = emptyList()
) {
    constructor(): this("", "", "", "", "", Gender.NONE)

    val allQuestionsAnswered: Map<String, List<String>>
        get() = _allQuestionsAnswered

    val trainingRoutines: List<TrainingRoutine>
        get() = _trainingRoutines

    fun addTrainingRoutines(newTrainingRoutines: List<TrainingRoutine>) {
        _trainingRoutines = newTrainingRoutines
    }

    fun changeAllAnsweredQuestions(newAnsweredQuestions: Map<String, List<String>>) {
        _allQuestionsAnswered = newAnsweredQuestions
    }
}