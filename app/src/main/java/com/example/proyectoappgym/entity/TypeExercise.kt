package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R

enum class TypeExercise(val id: Int, val idIcon: Int, val nameType: String) {
    MACHINES(4, R.drawable.machine_exercises, "Machine exercises"),
    WEIGHTLIFTING(3, R.drawable.strength_exercises, "Weightlifting exercises"),
    WEIGHTLIFTING_AT_HOME(3, R.drawable.strength_exercises, "Weightlifting exercises"),
    TENS(1, R.drawable.calistehenic_tens_exercises, "Tension exercises"),
    BASIC(2, R.drawable.calisthenic_basics_exercises, "Basic exercises"),
    CARDIO(5, R.drawable.cardio_exercises, "Cardio exercises");

    companion object {
        fun fromString(typeExercise: String) =
            when(typeExercise) {
                MACHINES.nameType -> MACHINES
                WEIGHTLIFTING.nameType -> WEIGHTLIFTING
                TENS.nameType -> TENS
                BASIC.nameType -> BASIC
                CARDIO.nameType -> CARDIO
                else -> MACHINES
            }

    }
}