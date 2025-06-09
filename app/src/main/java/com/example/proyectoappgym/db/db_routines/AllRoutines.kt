package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.ExercisesName
import com.example.proyectoappgym.entity.GroupMuscles

object AllRoutines {

    val machineExercises = mapOf(
        GroupMuscles.PUSH_UP to listOf(
            ExercisesName.CHEST_PRESS_MACHINE,
            ExercisesName.PECK_DECK_MACHINE,
            ExercisesName.INCLINE_PRESS_MACHINE,
            ExercisesName.SHOULDER_PRESS_MACHINE,
            ExercisesName.LATERAL_RAISE_MACHINE,
            ExercisesName.TRICEP_EXTENSION_MACHINE,
            ExercisesName.TRICEPS_ROPE_EXTENSION_ON_HIGH_PULLEY
        ),
        GroupMuscles.PULL_UP to listOf(
            ExercisesName.LAT_PULLDOWN_MACHINE,
            ExercisesName.SEATED_ROW_MACHINE,
            ExercisesName.PULL_OVER_MACHINE,
            ExercisesName.LEVER_SEATED_ROW,
            ExercisesName.BICEP_CURL_MACHINE,
            ExercisesName.PREACHER_CURL_MACHINE,
            ExercisesName.BACK_EXTENSION_MACHINE,
            ExercisesName.TRAPEZIUS_MACHINE
        ),
        GroupMuscles.LEGS to listOf(
            ExercisesName.LEG_PRESS_MACHINE,
            ExercisesName.LEG_EXTENSION_MACHINE,
            ExercisesName.LEG_CURL_MACHINE,
            ExercisesName.ABDUCTOR_MACHINE,
            ExercisesName.CABLE_PULL_THROUGH,
            ExercisesName.SQUATS,
            ExercisesName.STANDING_OR_SEATED_CALF_RAISES
        ),
        GroupMuscles.CORE to listOf(
            ExercisesName.AB_CRUNCH_MACHINE,
            ExercisesName.OBLIQUE_MACHINE,
            ExercisesName.CABLE_CRUNCH_MACHINE,
            ExercisesName.BACK_EXTENSION_MACHINE,
            ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE,
            ExercisesName.LEG_RAISES_WITH_WEIGHT
        )
    )

    val tensionExercises = mapOf(
        GroupMuscles.PUSH_UP to listOf(
            ExercisesName.PRESS_PLANK,
            ExercisesName.PUSH_UP_PLANK,
            ExercisesName.HANDSTAND_PUSH_UP,
            ExercisesName.MALTESE_PLANK_PUSH_UP
        ),
        GroupMuscles.PULL_UP to listOf(
            ExercisesName.PULL_UP_HOLDING_UP,
            ExercisesName.PULL_UP_FRONT_LEVER,
            ExercisesName.PRESS_FRONT_LEVER,
            ExercisesName.MALTESE_FRONT_LEVER_PRESS
        ),
        GroupMuscles.LEGS to listOf(
            ExercisesName.SQUATS,
            ExercisesName.JUMP_SQUATS,
            ExercisesName.LUNGES,
            ExercisesName.STEP_UPS,
            ExercisesName.CALF_RAISES
        ),
        GroupMuscles.CORE to listOf(
            ExercisesName.MOUNTAIN_CLIMBERS,
            ExercisesName.PLANK,
            ExercisesName.TOE_TOUCHES,
            ExercisesName.RUSSIAN_TWISTS,
            ExercisesName.CRUNCHES
        )
    )

    val basicExercises = mapOf(
        GroupMuscles.PUSH_UP to listOf(
            ExercisesName.PUSH_UPS,
            ExercisesName.DIPS,
            ExercisesName.CLOSED_GRIP_PUSH_UPS
        ),
        GroupMuscles.PULL_UP to listOf(
            ExercisesName.PULL_UPS,
            ExercisesName.CHIN_UPS,
            ExercisesName.NEGATIVE_PULL_UPS,
            ExercisesName.PULL_UP_HOLDING_UP
        ),
        GroupMuscles.LEGS to listOf(
            ExercisesName.SQUATS,
            ExercisesName.JUMP_SQUATS,
            ExercisesName.LUNGES,
            ExercisesName.STEP_UPS,
            ExercisesName.CALF_RAISES
        ),
        GroupMuscles.CORE to listOf(
            ExercisesName.MOUNTAIN_CLIMBERS,
            ExercisesName.PLANK,
            ExercisesName.TOE_TOUCHES,
            ExercisesName.RUSSIAN_TWISTS,
            ExercisesName.CRUNCHES
        )
    )

    val weightliftingExercises = mapOf(
        GroupMuscles.PUSH_UP to listOf(
            ExercisesName.BENCH_PRESS,
            ExercisesName.INCLINE_BENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS,
            ExercisesName.FLAT_BENCH_DUMBBELL_FLYES,
            ExercisesName.MILITARY_PRESS_WITH_BARBELL_OR_DUMBBELLS,
            ExercisesName.LATERAL_RAISES_WITH_DUMBBELLS,
            ExercisesName.FRONT_RAISES_WITH_DUMBBELLS_OR_BARBELL,
            ExercisesName.DIPS_WEIGHTED,
            ExercisesName.OVERHEAD_TRICEPS_EXTENSION_WITH_DUMBBELL,
            ExercisesName.TRICEPS_ROPE_EXTENSION_ON_HIGH_PULLEY,
            ExercisesName.FRENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS
        ),
        GroupMuscles.PULL_UP to listOf(
            ExercisesName.DEADLIFT,
            ExercisesName.BARBELL_ROW,
            ExercisesName.ONE_ARM_DUMBBELL_ROW,
            ExercisesName.DUMBBELL_PULLOVER,
            ExercisesName.PULL_UPS_WEIGHTED,
            ExercisesName.BARBELL_CURL,
            ExercisesName.DUMBBELL_CURL,
            ExercisesName.HAMMER_CURL_WITH_DUMBBELLS,
            ExercisesName.CONCENTRATION_CURL
        ),
        GroupMuscles.LEGS to listOf(
            ExercisesName.BARBELL_SQUATS,
            ExercisesName.LEG_EXTENSION_MACHINE,
            ExercisesName.ROMANIAN_DEADLIFT,
            ExercisesName.DUMBBELL_LUNGES,
            ExercisesName.STANDING_OR_SEATED_CALF_RAISES,
            ExercisesName.LEG_PRESS_MACHINE
        ),
        GroupMuscles.CORE to listOf(
            ExercisesName.WEIGHTED_CRUNCH,
            ExercisesName.LEG_RAISES_WITH_WEIGHT,
            ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE,
            ExercisesName.WEIGHTED_PLANK,
            ExercisesName.AB_CRUNCH_MACHINE,
            ExercisesName.CRUNCHES
        )
    )
}