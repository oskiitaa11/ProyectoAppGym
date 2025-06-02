package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.db.retrofit.entity.ExercisesName

object AllRoutines {
    /*val routinesBasicTensionPushUp = listOf(
        ExercisesName.PUSH_UPS,                     // Flexiones clásicas
        ExercisesName.DIPS,                         // Fondos en paralelas
        ExercisesName.CLOSED_GRIP_PUSH_UPS,        // Flexiones con agarre cerrado (más tríceps)
        ExercisesName.MOUNTAIN_CLIMBERS,          // Core + empuje dinámico
        ExercisesName.PLANK,                       // Core estabilizador
        ExercisesName.PRESS_PLANK,                                 // Plancha activa de empuje
        ExercisesName.PUSH_UP_PLANK,                               // Transición de plancha a flexión
        ExercisesName.MALTESE_PLANK_PUSH_UP,                       // Variante avanzada de flexión estilo plancha maltesa
        ExercisesName.HANDSTAND_PUSH_UP                            // Flexiones en pino (para hombros y tríceps)
    )
    val routinesBasicTensionPullUp = listOf(
        ExercisesName.PULL_UPS,
        ExercisesName.CHIN_UPS,
        ExercisesName.NEGATIVE_PULL_UPS,
        ExercisesName.PULL_UP_HOLDING_UP,
        ExercisesName.PULL_UP_FRONT_LEVER,
        ExercisesName.BENCH_DIPS,
        ExercisesName.PRESS_FRONT_LEVER,
        ExercisesName.MALTESE_FRONT_LEVER_PRESS
    )

    val routineBasicTensionLegsCore =  listOf(
        ExercisesName.SQUATS,                   // Sentadillas clásicas
        ExercisesName.JUMP_SQUATS,              // Sentadillas con salto (explosividad)
        ExercisesName.LUNGES,                   // Zancadas
        ExercisesName.STEP_UPS,                 // Subida a banco o caja
        ExercisesName.CALF_RAISES,              // Elevaciones de talones (gemelos)
        ExercisesName.MOUNTAIN_CLIMBERS,        // Cardio + activación de piernas
        ExercisesName.PLANK,                    // Core estabilizador
        ExercisesName.TOE_TOUCHES, // Trabajo abdominal y movilidad posterior
        ExercisesName.CRUNCHES,
        ExercisesName.RUSSIAN_TWISTS
    )*/

    val routineTensionPushUp = listOf(
        ExercisesName.PRESS_PLANK,                                 // Plancha activa de empuje
        ExercisesName.PUSH_UP_PLANK,                               // Transición de plancha a flexión
        ExercisesName.HANDSTAND_PUSH_UP,                            // Flexiones en pino (para hombros y tríceps)
        ExercisesName.MALTESE_PLANK_PUSH_UP,
    )

    val routineTensionPullUp = listOf(
        ExercisesName.PULL_UP_HOLDING_UP,
        ExercisesName.PULL_UP_FRONT_LEVER,
        ExercisesName.PRESS_FRONT_LEVER,
        ExercisesName.MALTESE_FRONT_LEVER_PRESS
    )

    val routineBasicLegs = listOf(
        ExercisesName.SQUATS,                   // Sentadillas clásicas
        ExercisesName.JUMP_SQUATS,              // Sentadillas con salto (explosividad)
        ExercisesName.LUNGES,                   // Zancadas
        ExercisesName.STEP_UPS,                 // Subida a banco o caja
        ExercisesName.CALF_RAISES
    )

    val routineBasicCore = listOf(
        ExercisesName.MOUNTAIN_CLIMBERS,        // Cardio + activación de piernas
        ExercisesName.PLANK,                    // Core estabilizador
        ExercisesName.TOE_TOUCHES,
        ExercisesName.RUSSIAN_TWISTS,
        ExercisesName.CRUNCHES
    )

    val routineBasicPullUp = listOf(
        ExercisesName.PULL_UPS,
        ExercisesName.CHIN_UPS,
        ExercisesName.NEGATIVE_PULL_UPS,
        ExercisesName.PULL_UP_HOLDING_UP,
        ExercisesName.BENCH_DIPS
    )

    val routineBasicPushUp = listOf(
        ExercisesName.PUSH_UPS,                     // Flexiones clásicas
        ExercisesName.DIPS,                         // Fondos en paralelas
        ExercisesName.CLOSED_GRIP_PUSH_UPS,
    )

    val routineWeightliftingPushUp = listOf(
        ExercisesName.FLAT_BARBELL_BENCH_PRESS,                      // Press de banca plano con barra
        ExercisesName.INCLINE_BENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS, // Press inclinado con barra o mancuernas
        ExercisesName.FLAT_BENCH_DUMBBELL_FLYES,                     // Aperturas con mancuernas en banco plano
        ExercisesName.MILITARY_PRESS_WITH_BARBELL_OR_DUMBBELLS,      // Press militar (hombros)
        ExercisesName.LATERAL_RAISES_WITH_DUMBBELLS,                 // Elevaciones laterales
        ExercisesName.FRONT_RAISES_WITH_DUMBBELLS_OR_BARBELL,        // Elevaciones frontales
        ExercisesName.DIPS_WEIGHTED,                                 // Fondos lastrados
        ExercisesName.OVERHEAD_TRICEPS_EXTENSION_WITH_DUMBBELL,      // Extensión de tríceps por encima de la cabeza
        ExercisesName.TRICEPS_ROPE_EXTENSION_ON_HIGH_PULLEY,         // Extensión de tríceps en polea con cuerda
        ExercisesName.FRENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS
    )

    val routineWeightLiftingPullUp = listOf(
        ExercisesName.DEADLIFT,                              // Peso muerto (espalda baja y cadena posterior)
        ExercisesName.BARBELL_ROW,                           // Remo con barra
        ExercisesName.ONE_ARM_DUMBBELL_ROW,                  // Remo con mancuerna a una mano
        ExercisesName.DUMBBELL_PULLOVER,                     // Pullover con mancuerna (dorsales y pecho)
        ExercisesName.LAT_PULLDOWN_CABLE,                    // Jalón en polea alta
        ExercisesName.PULL_UPS_WEIGHTED,                     // Dominadas lastradas
        ExercisesName.BARBELL_CURL,                          // Curl de bíceps con barra
        ExercisesName.DUMBBELL_CURL,                         // Curl de bíceps con mancuernas
        ExercisesName.HAMMER_CURL_WITH_DUMBBELLS,            // Curl martillo (braquiorradial)
        ExercisesName.CONCENTRATION_CURL                     // Curl de concentración (aislamiento de bíceps)
    )

    val routineWeightliftingLegs = listOf(
        ExercisesName.BARBELL_SQUATS,                         // Sentadillas con barra
        ExercisesName.LEG_PRESS,                              // Prensa de piernas
        ExercisesName.ROMANIAN_DEADLIFT,                      // Peso muerto rumano (femorales/glúteos)
        ExercisesName.DUMBBELL_LUNGES,                        // Zancadas con mancuernas
        ExercisesName.STANDING_OR_SEATED_CALF_RAISES          // Elevaciones de talones (gemelos de pie o sentado)
    )

    val routineWeightliftingCore = listOf(
        ExercisesName.WEIGHTED_CRUNCH,                         // Crunch con peso
        ExercisesName.LEG_RAISES_WITH_WEIGHT,                  // Elevaciones de piernas con peso
        ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE,    // Giros rusos con mancuerna o disco
        ExercisesName.WEIGHTED_PLANK,                          // Plancha con peso
    )

    val routineMachinesExercisesPushUp = listOf(
        ExercisesName.CHEST_PRESS_MACHINE,
        ExercisesName.PECK_DECK_MACHINE,
        ExercisesName.INCLINE_PRESS_MACHINE,
        ExercisesName.SHOULDER_PRESS_MACHINE,
        ExercisesName.LATERAL_RAISE_MACHINE,
        ExercisesName.TRICEP_EXTENSION_MACHINE,
        ExercisesName.TRAPEZIUS_MACHINE
    )

    val routineMachineExercisesPullUp = listOf(
        ExercisesName.LAT_PULLDOWN_MACHINE,
        ExercisesName.SEATED_ROW_MACHINE,
        ExercisesName.PULL_OVER_MACHINE,
        ExercisesName.LEVER_SEATED_ROW,
        ExercisesName.BICEP_CURL_MACHINE,
        ExercisesName.PREACHER_CURL_MACHINE,
        ExercisesName.BACK_EXTENSION_MACHINE
    )
    val routineMachineExercisesLegs = listOf(
        ExercisesName.LEG_PRESS_MACHINE,
        ExercisesName.LEG_EXTENSION_MACHINE,
        ExercisesName.LEG_CURL_MACHINE,
        ExercisesName.ABDUCTOR_MACHINE,
        ExercisesName.CABLE_PULL_THROUGH
    )

    val routineMachineExercisesCore = listOf(
        ExercisesName.AB_CRUNCH_MACHINE,
        ExercisesName.OBLIQUE_MACHINE,
        ExercisesName.CABLE_CRUNCH_MACHINE,
        ExercisesName.BACK_EXTENSION_MACHINE
    )
}