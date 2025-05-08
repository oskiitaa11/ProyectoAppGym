package com.example.proyectoappgym.entity

data class Exercise(
    val name: String,
    val description: String,
    val type: TypeExercise,
    val trainedMuscles: List<String>,
    val dayOfWeek: DayOfWeek,
    val series: Int,
    val repeticiones: String,
    val descansoSegundos: Int
)