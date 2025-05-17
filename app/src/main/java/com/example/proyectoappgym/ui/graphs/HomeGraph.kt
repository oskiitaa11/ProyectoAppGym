package com.example.proyectoappgym.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.proyectoappgym.ui.screens.HomeRoute
import com.example.proyectoappgym.ui.screens.exerciseDestination
import com.example.proyectoappgym.ui.screens.homeDestination
import kotlinx.serialization.Serializable

@Serializable
object HomeGraphRoute

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<HomeGraphRoute>(startDestination = HomeRoute) {
        homeDestination(navController)
        exerciseDestination()
    }
}