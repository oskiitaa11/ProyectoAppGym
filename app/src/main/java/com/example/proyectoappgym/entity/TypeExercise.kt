package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R

enum class TypeExercise(val idIcon: Int, val nameType: String) {
    MACHINES(R.drawable.machine_exercises, "Machine exercises"),
    WEIGHTLIFTING(R.drawable.strength_exercises, "Weightlifting exercises"),
    TENS(R.drawable.calistehenic_tens_exercises, "Tension exercises"),
    BASIC(R.drawable.calisthenic_basics_exercises, "Basic exercises"),
    CARDIO(R.drawable.cardio_exercises, "Cardio exercises")
}