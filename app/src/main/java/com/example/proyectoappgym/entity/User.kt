package com.example.proyectoappgym.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val username: String, val password: String, val email: String, val birthdate: String)