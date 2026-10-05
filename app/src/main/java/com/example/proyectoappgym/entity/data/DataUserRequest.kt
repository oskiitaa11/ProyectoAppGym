package com.example.proyectoappgym.entity.data

import com.example.proyectoappgym.entity.users.Gender
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataUserRequest(
    val username: String,
    var name: String,
    val birthdate: String,
    val gender: Gender,
    var allQuestionsAnswered: Map<String, List<String>> = mapOf(),
    @SerializedName("profileAvatar")
    @Expose
    var idProfileAvatar: Int = 1
)