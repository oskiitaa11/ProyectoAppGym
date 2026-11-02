package com.example.proyectoappgym.entity.data.request

import com.example.proyectoappgym.entity.users.Gender
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataUserRequest(
    val username: String,
    @SerializedName("full_name")
    val name: String,
    @SerializedName("birth_date")
    val birthdate: String,
    val gender: Gender,
    @SerializedName("all_answered_questions")
    val allQuestionsAnswered: Map<String, List<String>> = mapOf(),
    @SerializedName("id_profile_avatar")
    @Expose
    val idProfileAvatar: Int = 1
)