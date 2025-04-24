package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R

data class User(
    val username: String,
    var password: String,
    val email: String,
    val name: String,
    val birthdate: String,
    val gender: Gender,
    var allQuestionsAnswered: Map<String, List<String>> = mapOf(),
    var profileAvatar: Int = R.drawable.avatar1
) {
    constructor(): this("", "", "", "", "", Gender.NONE, mapOf())
}