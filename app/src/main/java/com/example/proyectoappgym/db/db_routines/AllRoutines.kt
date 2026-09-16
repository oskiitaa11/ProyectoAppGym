package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.ExercisesDefault
import com.example.proyectoappgym.entity.ExercisesName
import com.example.proyectoappgym.entity.GroupMuscles
import com.example.proyectoappgym.entity.RealizationExercise
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.entity.TypeTensExercise
import com.example.proyectoappgym.ui.screens.ExerciseScreen

object AllRoutines {

    val weightliftingLegExercises = listOf(
        ExercisesName.STEP_UP_WITH_DUMBBELLS,
        ExercisesName.BARBELL_SQUATS,
        ExercisesName.DUMBBELL_LUNGES,
        ExercisesName.STANDING_OR_SEATED_CALF_RAISES
    )

    val machinesLegsExercises = listOf(
        ExercisesName.LEG_PRESS_MACHINE,
        ExercisesName.LEG_EXTENSION_MACHINE,
        ExercisesName.LEG_CURL_MACHINE,
        ExercisesName.ABDUCTOR_MACHINE,
        ExercisesName.CABLE_PULL_THROUGH
    )

    val machinesCoreExercises = listOf(
        ExercisesName.CABLE_CRUNCH_MACHINE,
        ExercisesName.OBLIQUE_MACHINE
    )

    val weightliftingCoreExercises = listOf(
        ExercisesName.WEIGHTED_CRUNCH,
        ExercisesName.LEG_RAISES_WITH_WEIGHT,
        ExercisesName.MOUNTAIN_CLIMBERS,
        ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE,
        ExercisesName.TOE_TOUCHES
    )

    val calisthenicsLegsExercises = listOf(
        ExercisesName.SQUATS,
        ExercisesName.JUMP_SQUATS,
        ExercisesName.LUNGES,
        ExercisesName.STEP_UPS,
        ExercisesName.CALF_RAISES
    )

    val calisthenicsCoreExercises = listOf(
        ExercisesName.CRUNCHES,
        ExercisesName.RUSSIAN_TWISTS,
        ExercisesName.TOE_TOUCHES
    )

    val tensExercises = listOf(
        ExercisesName.COMBOS_TENSION,
        ExercisesName.PUSH_UP_PLANK,
        ExercisesName.PRESS_PLANK,
        ExercisesName.HANDSTAND_PUSH_UP,
        ExercisesName.MALTESE_PLANK_PUSH_UP,
        ExercisesName.PRESS_FRONT_LEVER,
        ExercisesName.PULL_UP_FRONT_LEVER,
        ExercisesName.MALTESE_FRONT_LEVER_PRESS,
        ExercisesName.PULL_UP_HOLDING_UP
    )

    val calisthenicsExerciseBasic = listOf(
        ExercisesName.PUSH_UPS,
        ExercisesName.DIPS,
        ExercisesName.DIPS_ON_STRAIGHT_BAR,
        ExercisesName.CLOSED_GRIP_PUSH_UPS,
        ExercisesName.PIKE_PUSH_UPS,
        ExercisesName.PULL_UPS,
        ExercisesName.CHIN_UPS,
        ExercisesName.ARCHER_PULL_UPS_BACK,
        ExercisesName.TRICEPS_CLOSED_PUSH_UPS
    )
}