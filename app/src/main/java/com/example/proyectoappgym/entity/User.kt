package com.example.proyectoappgym.entity

data class User(
    val username: String,
    var password: String,
    val email: String,
    val name: String,
    val birthdate: String,
    val gender: Gender,
    var allQuestionsAnswered: Map<String, List<String>> = mapOf()
) {

}