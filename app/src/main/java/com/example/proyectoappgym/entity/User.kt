package com.example.proyectoappgym.entity

import kotlinx.serialization.Serializable


data class User(
    val username: String,
    val password: String,
    val email: String,
    val name: String,
    val birthdate: String,
    val gender: Gender
)