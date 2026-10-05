package com.example.proyectoappgym.entity.realizationexercises

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val description: String,
    val exerciseLevel: ExerciseLevel,
    val type: TypeExercise,
    val trainedPrimaryMuscles: List<Muscles>,
    val nameVideo: String,
    val idImageMuscles: Int,
    val idCoverImage: Int,
    val stepsForDoIt: String,
    val typeTensExercise: TypeTensExercise? = null,
)