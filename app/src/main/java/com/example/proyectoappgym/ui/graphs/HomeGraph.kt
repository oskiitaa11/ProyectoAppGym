package com.example.proyectoappgym.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.proyectoappgym.ui.screens.ExerciseRoute
import com.example.proyectoappgym.ui.screens.HomeRoute
import com.example.proyectoappgym.ui.screens.exerciseDestination
import com.example.proyectoappgym.ui.screens.goToExerciseRoute
import com.example.proyectoappgym.ui.screens.goToProfileScreen
import com.example.proyectoappgym.ui.screens.homeDestination
import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
object HomeGraphRoute

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<HomeGraphRoute>(startDestination = HomeRoute) {
        homeDestination({ exercise -> navController.goToExerciseRoute(exercise) }, { navController.goToProfileScreen() })
        exerciseDestination { navController.popBackStack() }
    }
}