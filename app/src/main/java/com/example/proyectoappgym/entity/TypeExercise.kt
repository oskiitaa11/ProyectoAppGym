package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R

enum class TypeExercise(val idIcon: Int, val nameType: String) {
    MACHINES(R.drawable.machine_exercises, "Machine exercise"), WEIGHTLIFTING(R.drawable.strength_exercises, "Weightlifting exercise"), TENS(R.drawable.calistehenic_tens_exercises, "Tension exercise"), BASIC(R.drawable.calisthenic_basics_exercises, "Basic exercise"), CARDIO(R.drawable.cardio_exercises, "Cardio exercise")
}