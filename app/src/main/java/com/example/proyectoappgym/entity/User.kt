package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

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
    var profileAvatar: Avatars = Avatars.AVATAR1,
    var trainingRoutines: List<TrainingRoutine> = emptyList<TrainingRoutine>()
) {
    constructor(): this("", "", "", "", "", Gender.NONE)
}