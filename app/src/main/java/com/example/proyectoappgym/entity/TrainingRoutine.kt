package com.example.proyectoappgym.entity

data class TrainingRoutine(
    val dayOfWeek: DayOfWeek,
    val name: String,
    val exercises: List<Exercise>
)